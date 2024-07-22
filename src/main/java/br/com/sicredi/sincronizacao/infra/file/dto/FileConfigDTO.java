package br.com.sicredi.sincronizacao.infra.file.dto;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

@Data
@Builder
public class FileConfigDTO<T> {
    private Path path;
    private String splitter;
    private Class<T> classType;
}
