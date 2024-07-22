package br.com.sicredi.sincronizacao.mappers;

import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;

public interface FileConfigMapper<T,R>{
    IOFileConfigDTO<T, R> mapToIOFileConfig(String filePath);
}
