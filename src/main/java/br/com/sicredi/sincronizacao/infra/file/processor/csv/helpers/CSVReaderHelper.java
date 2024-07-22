package br.com.sicredi.sincronizacao.infra.file.processor.csv.helpers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class CSVReaderHelper<T> {

    public BufferedReader createBufferedReader(Path filePath) throws IOException {
        return Files.newBufferedReader(filePath);
    }

    public CSVReader createCSVReader(BufferedReader reader, char separator) {
        return new CSVReaderBuilder(reader)
                .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                .build();
    }
}