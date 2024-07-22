package br.com.sicredi.sincronizacao.infra.file.processor.csv.helpers;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CSVWriterHelper<R> {

    public Writer createCSVWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }

    public StatefulBeanToCsv<R> getBeanToCsv(Writer writer, String csvSplitter,Class<R> clazz) {
        String[] headers = this.generateOutputHeader(clazz);
        var strategy = this.gettingStrategyConfig(headers,clazz);
        return new StatefulBeanToCsvBuilder<R>(writer)
                .withSeparator(csvSplitter.charAt(0))
                .withMappingStrategy(strategy)
                .build();
    }

    private ColumnPositionMappingStrategy<R> gettingStrategyConfig(String[] header, Class<R> clazz) {
        ColumnPositionMappingStrategy<R> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(clazz);
        strategy.setColumnMapping(header);
        return strategy;
    }

    public String[] generateOutputHeader(Class<?> clazzR) {
        List<String> headers = new ArrayList<>();
        Class<?> current = clazzR;
        List<Class<?>> classes = new ArrayList<>();
        while (current.getSuperclass() != null) {
            classes.add(current);
            current = current.getSuperclass();
        }
        Collections.reverse(classes);
        classes.forEach(aClass -> {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(CsvBindByName.class)) {
                    CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
                    headers.add(annotation.column());
                }
            }
        });
        return headers.toArray(new String[0]);
    }

    public String getHeader(Class<R> clazzR, String csvSplitter) {
        String[] outputHeader = this.generateOutputHeader(clazzR);
        log.info("Cabeçalho do arquivo de saída gerado.");
        return String.join(String.valueOf(csvSplitter.charAt(0)), outputHeader) + "\n";
    }
}