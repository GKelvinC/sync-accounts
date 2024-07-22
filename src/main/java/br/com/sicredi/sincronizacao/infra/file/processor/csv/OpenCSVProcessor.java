package br.com.sicredi.sincronizacao.infra.file.processor.csv;

import br.com.sicredi.sincronizacao.domain.dto.SyncAccountsParametersDTO;
import br.com.sicredi.sincronizacao.infra.gateways.FileProcessorGateway;
import br.com.sicredi.sincronizacao.infra.file.dto.FileConfigDTO;
import br.com.sicredi.sincronizacao.infra.file.processor.csv.helpers.CSVMapperHelper;
import br.com.sicredi.sincronizacao.infra.file.processor.csv.helpers.CSVReaderHelper;
import br.com.sicredi.sincronizacao.infra.file.processor.csv.helpers.CSVWriterHelper;
import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


@Slf4j
@Component
@RequiredArgsConstructor
public class OpenCSVProcessor<T, R> implements FileProcessorGateway<T, R> {

    @Value("${csv.config.default.output_file}")
    private String defaultOutputPath;

    @Value("${csv.config.default.splitter}")
    private String defaultSplitter;

    private final CSVThreadPoolConfig csvThreadPoolConfig;
    private final CSVReaderHelper<T> csvReaderHelper;
    private final CSVWriterHelper<R> csvWriterHelper;
    private final CSVMapperHelper<T,R> csvMapperHelper;

    @Override
    public void process(final IOFileConfigDTO<T,R> ioInputConfig,
                        final SyncAccountsParametersDTO execParam,
                        final Function<T, R> processFunction) {

        FileConfigDTO<T> fileInputConfig = ioInputConfig.getInputConfig();
        FileConfigDTO<R> fileOutputConfig = ioInputConfig.getOutputConfig();

        String csvInputSplitter =  fileInputConfig.getSplitter() != null ? fileInputConfig.getSplitter() : defaultSplitter;
        String csvOutputSplitter =  fileOutputConfig.getSplitter() != null ? fileOutputConfig.getSplitter() : defaultSplitter;
        String csvOutputPath = fileOutputConfig.getPath() != null ? fileOutputConfig.getPath().toString() : defaultOutputPath;

        ThreadPoolExecutor executor = csvThreadPoolConfig.createThreadPoolExecutor(
                String.valueOf(execParam.getMinNumThreads()),
                String.valueOf(execParam.getMaxNumThreads()),
                String.valueOf(execParam.getKeepAliveTime()));

        try (BufferedReader reader = csvReaderHelper.createBufferedReader(fileInputConfig.getPath());
             CSVReader csvReader = csvReaderHelper.createCSVReader(reader, csvInputSplitter.charAt(0));
             Writer writer = csvWriterHelper.createCSVWriter(csvOutputPath)) {

            String[] inputHeaders = csvReader.readNext();

            String[] outputHeaders = csvWriterHelper.generateOutputHeader(fileOutputConfig.getClassType());
            String outputHeader = String.join(String.valueOf(csvOutputSplitter.charAt(0)), outputHeaders) + "\n";

            writer.write(outputHeader);

            String[] record;
            while ((record = csvReader.readNext()) != null) {
                String[] finalRecord = record;
                executor.execute(() -> processCsvRecord(finalRecord, inputHeaders,outputHeaders,csvOutputSplitter, processFunction, writer, fileInputConfig.getClassType()));
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(Long.parseLong(csvThreadPoolConfig.getTimeoutInSeconds()), TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("Erro ao aguardar a conclusão das tarefas: ", e);
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }

            log.info("Processamento do arquivo CSV concluído.");

        } catch (IOException | CsvValidationException e) {
            log.error("Erro durante o processamento do arquivo CSV: ", e);
        }
    }


    private void processCsvRecord(String[] record,
                                  String[] inputHeaders,
                                  String[] outputHeaders,
                                  String csvOutputSplitter,
                                  Function<T, R> processFunction,
                                  Writer writer,
                                  Class<T> clazzT) {
        T parsedRecord = csvMapperHelper.mapRecordToBean(record, inputHeaders, clazzT);
        if (parsedRecord != null) {
            R processedRecord = processFunction.apply(parsedRecord);
            String processedRecordString = csvMapperHelper.mapBeanToRecord(processedRecord,outputHeaders,csvOutputSplitter);
            synchronized (this) {
                try {
                    writer.write(processedRecordString);
                } catch (IOException e) {
                    log.error("Erro ao escrever registro processado no CSV de saída: ", e);
                }
            }
        }
    }
}