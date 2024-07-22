package br.com.sicredi.sincronizacao.application.gateways;

import br.com.sicredi.sincronizacao.domain.dto.SyncAccountsParametersDTO;

public interface SyncGateway {

    void syncAccounts(SyncAccountsParametersDTO parametersDTO);
}
