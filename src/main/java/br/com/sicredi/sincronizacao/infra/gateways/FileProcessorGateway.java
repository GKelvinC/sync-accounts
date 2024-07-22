package br.com.sicredi.sincronizacao.infra.gateways;

import br.com.sicredi.sincronizacao.domain.dto.SyncAccountsParametersDTO;
import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;

import java.util.function.Function;

public interface FileProcessorGateway<T,R> {

    void process(
            final IOFileConfigDTO<T, R> ioInputConfig,
            final SyncAccountsParametersDTO execParameters,
            final Function<T, R> processFunction
    );
}
