package br.com.sicredi.sincronizacao.infra.gateways;

import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;

public interface BacenAccountGateway {
    boolean atualizaConta(AccountDTO contaDTO);
}
