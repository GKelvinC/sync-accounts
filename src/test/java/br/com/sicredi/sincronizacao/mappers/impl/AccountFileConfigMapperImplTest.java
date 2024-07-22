package br.com.sicredi.sincronizacao.mappers.impl;

import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;
import br.com.sicredi.sincronizacao.domain.dto.AccountProcessedDTO;
import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;


public class AccountFileConfigMapperImplTest {

    @Test
    void syncAccountsTest_ValidFile_Path() {
        AccountFileConfigMapperImpl fileConfigMapper = new AccountFileConfigMapperImpl();
        String filePath = "test.csv";

        IOFileConfigDTO<AccountDTO, AccountProcessedDTO> actual = fileConfigMapper.mapToIOFileConfig(filePath);

        String expectedProcessedFilePath = "test-Processado.csv";

        assertEquals(Paths.get(filePath), actual.getInputConfig().getPath());
        assertEquals(AccountDTO.class, actual.getInputConfig().getClassType());
        assertEquals(",", actual.getInputConfig().getSplitter());

        assertEquals(Paths.get(expectedProcessedFilePath), actual.getOutputConfig().getPath());
        assertEquals(AccountProcessedDTO.class, actual.getOutputConfig().getClassType());
    }

    @Test
    void syncAccountsTest_InvalidFile_Path() {
        AccountFileConfigMapperImpl fileConfigMapper = new AccountFileConfigMapperImpl();
        String filePath = "non-existing-file.csv";

        IOFileConfigDTO<AccountDTO, AccountProcessedDTO> actual = fileConfigMapper.mapToIOFileConfig(filePath);

        String expectedProcessedFilePath = "non-existing-file-Processado.csv";

        assertEquals(Paths.get(filePath), actual.getInputConfig().getPath());
        assertEquals(AccountDTO.class, actual.getInputConfig().getClassType());
        assertEquals(",", actual.getInputConfig().getSplitter());

        assertEquals(Paths.get(expectedProcessedFilePath), actual.getOutputConfig().getPath());
        assertEquals(AccountProcessedDTO.class, actual.getOutputConfig().getClassType());
    }
}