package br.com.sicredi.sincronizacao.application.usecases;

import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;
import br.com.sicredi.sincronizacao.domain.dto.AccountProcessedDTO;
import br.com.sicredi.sincronizacao.domain.dto.SyncAccountsParametersDTO;
import br.com.sicredi.sincronizacao.infra.gateways.BacenAccountGateway;
import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;
import br.com.sicredi.sincronizacao.infra.file.validator.FileValidator;
import br.com.sicredi.sincronizacao.infra.gateways.FileProcessorGateway;
import br.com.sicredi.sincronizacao.mappers.FileConfigMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncFileUseCaseTest {

    @Mock
    private BacenAccountGateway bacenAccountGateway;

    @Mock
    private FileProcessorGateway<AccountDTO, AccountProcessedDTO> accountFileProcessor;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private FileConfigMapper<AccountDTO, AccountProcessedDTO> fileConfigMapper;

    @Mock
    private IOFileConfigDTO<AccountDTO, AccountProcessedDTO> ioFileConfigDTO;

    @InjectMocks
    private SyncFileUseCase syncFileUseCase;

    @Test
    public void syncAccountsTest_ValidFile_Path() {
        SyncAccountsParametersDTO parametersDTO = new SyncAccountsParametersDTO();
        parametersDTO.setFilePaths(new String[]{"path/to/file1.csv", "path/to/file2.csv"});
        
        when(fileValidator.isValid(anyString())).thenReturn(true);
        
        when(fileConfigMapper.mapToIOFileConfig(anyString())).thenReturn(ioFileConfigDTO);
        
        syncFileUseCase.syncAccounts(parametersDTO);
        
        verify(accountFileProcessor, times(2)).process(eq(ioFileConfigDTO), eq(parametersDTO), any());
    }

    @Test
    public void syncAccountsTest_InValidFile_Path() {
        SyncAccountsParametersDTO parametersDTO = new SyncAccountsParametersDTO();
        parametersDTO.setFilePaths(new String[]{"invalid/path/to/file.csv"});
        
        when(fileValidator.isValid(anyString())).thenReturn(false);
        
        syncFileUseCase.syncAccounts(parametersDTO);
        
        verify(accountFileProcessor, times(0)).process(any(), any(), any());
    }

    @Test
    public void processAccountTest_ValidData() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAgencia("1234");
        accountDTO.setConta("5678");
        accountDTO.setSaldo(1500.0);

        when(bacenAccountGateway.atualizaConta(any())).thenReturn(true);

        AccountProcessedDTO accountProcessedDTO = syncFileUseCase.processAccount(accountDTO);

        verify(bacenAccountGateway, times(1)).atualizaConta(accountDTO);
        assertEquals(accountProcessedDTO.getAgencia(), accountDTO.getAgencia());
        assertEquals(accountProcessedDTO.getConta(), accountDTO.getConta());
        assertEquals(accountProcessedDTO.getSaldo(), accountDTO.getSaldo());
        assertTrue(accountProcessedDTO.getStatus());
    }

    @Test
    public void processAccountTest_InvalidData() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAgencia("1234");
        accountDTO.setConta("5678");
        accountDTO.setSaldo(1500.0);

        when(bacenAccountGateway.atualizaConta(any())).thenThrow(new IllegalArgumentException());

        AccountProcessedDTO accountProcessedDTO = syncFileUseCase.processAccount(accountDTO);

        verify(bacenAccountGateway, times(1)).atualizaConta(accountDTO);
        assertEquals(accountProcessedDTO.getAgencia(), accountDTO.getAgencia());
        assertEquals(accountProcessedDTO.getConta(), accountDTO.getConta());
        assertEquals(accountProcessedDTO.getSaldo(), accountDTO.getSaldo());
        assertFalse(accountProcessedDTO.getStatus());
   }
}
