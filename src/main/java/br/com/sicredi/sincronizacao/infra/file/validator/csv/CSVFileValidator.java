package br.com.sicredi.sincronizacao.infra.file.validator.csv;

import br.com.sicredi.sincronizacao.domain.enums.FileType;
import br.com.sicredi.sincronizacao.infra.file.validator.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class CSVFileValidator implements FileValidator {
    @Override
    public boolean isValid(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return isFileExists(path) && isCsvFile(path);
        } catch (InvalidPathException | NullPointerException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    private boolean isFileExists(Path path) {
        return Files.exists(path);
    }

    private boolean isCsvFile(Path path) {
        String fileName = path.getFileName().toString();

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return false;
        }
        String extension = fileName.substring(dotIndex + 1);
        return FileType.CSV.getType().equalsIgnoreCase(extension);
    }
}
