package br.com.sicredi.sincronizacao.infra.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IOFileConfigDTO<T, R> {
    private FileConfigDTO<T> inputConfig;
    private FileConfigDTO<R> outputConfig;
}
