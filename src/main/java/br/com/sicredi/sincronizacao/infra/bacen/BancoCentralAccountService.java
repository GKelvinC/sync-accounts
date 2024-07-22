package br.com.sicredi.sincronizacao.infra.bacen;

import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;
import br.com.sicredi.sincronizacao.infra.gateways.BacenAccountGateway;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BancoCentralAccountService implements BacenAccountGateway {

  private static final int MIN_AGENCIA_LENGTH = 4;

  private static final int MIN_CONTA_LENGTH = 7;

  private final Random random = new Random();

  /**
   * Realiza o envio da conta para atualização junto ao Banco Central
   * @param contaDTO dados da conta a ser atualizada
   * @return <b>true</b> se a conta foi atualizada ou <b>false</b> caso contrário.
   */
  public boolean atualizaConta(AccountDTO contaDTO) {
    validarConta(contaDTO);
    return random.nextBoolean();
  }

  private void validarConta(AccountDTO contaDTO) {
    if(StringUtils.isAnyBlank(contaDTO.getAgencia(), contaDTO.getConta())
       && !validarTamanhos(contaDTO.getAgencia(), contaDTO.getConta())) {
      throw new IllegalArgumentException();
    }
  }

  private boolean validarTamanhos(String agencia, String conta) {
    return agencia.length() >= MIN_AGENCIA_LENGTH && conta.length() >= MIN_CONTA_LENGTH;
  }
}
