package br.com.sicredi.sincronizacao.application.usecases;

import br.com.sicredi.sincronizacao.application.gateways.SyncGateway;
import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;
import br.com.sicredi.sincronizacao.domain.dto.AccountProcessedDTO;
import br.com.sicredi.sincronizacao.domain.dto.SyncAccountsParametersDTO;
import br.com.sicredi.sincronizacao.infra.gateways.BacenAccountGateway;
import br.com.sicredi.sincronizacao.infra.gateways.FileProcessorGateway;
import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;
import br.com.sicredi.sincronizacao.infra.file.validator.FileValidator;
import br.com.sicredi.sincronizacao.mappers.FileConfigMapper;
import br.com.sicredi.sincronizacao.util.timer.MeasuredExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncFileUseCase implements SyncGateway {

  private final BacenAccountGateway bacenAccountGateway;

  private final FileValidator fileValidator;

  private final FileProcessorGateway<AccountDTO, AccountProcessedDTO> accountFileProcessor;

  private final FileConfigMapper<AccountDTO, AccountProcessedDTO> fileConfigMapper;

  @MeasuredExecutionTime
  public void syncAccounts(SyncAccountsParametersDTO parametersDTO) {
    for (String filePath : parametersDTO.getFilePaths()) {
      boolean isValid = fileValidator.isValid(filePath);
      if (isValid) {
        IOFileConfigDTO<AccountDTO, AccountProcessedDTO> ioFileConfig = fileConfigMapper.mapToIOFileConfig(filePath);
        accountFileProcessor.process(ioFileConfig, parametersDTO,this::processAccount);
      }
    }
  }

  public AccountProcessedDTO processAccount(AccountDTO accountDTO) {
    boolean status;
    try {
      status = bacenAccountGateway.atualizaConta(accountDTO);
    } catch (IllegalArgumentException e) {
      status = false;
    }
    return new AccountProcessedDTO(accountDTO.getAgencia(), accountDTO.getConta(), accountDTO.getSaldo(), status);
  }

}
