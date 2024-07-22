package br.com.sicredi.sincronizacao.infra.file.validator;

public interface FileValidator {

    boolean isValid(
            final String filePath);
}
