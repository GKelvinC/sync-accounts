package br.com.sicredi.sincronizacao.mappers.impl;

import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;
import br.com.sicredi.sincronizacao.domain.dto.AccountProcessedDTO;
import br.com.sicredi.sincronizacao.infra.file.dto.FileConfigDTO;
import br.com.sicredi.sincronizacao.infra.file.dto.IOFileConfigDTO;
import br.com.sicredi.sincronizacao.mappers.FileConfigMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class AccountFileConfigMapperImpl implements FileConfigMapper<AccountDTO, AccountProcessedDTO> {
    @Override
    public IOFileConfigDTO<AccountDTO, AccountProcessedDTO> mapToIOFileConfig(String filePath) {

        Path inputPath = Paths.get(filePath);

        String processedFilePath = filePath.replace(".csv", "-Processado.csv");
        Path outputPath = Paths.get(processedFilePath);

        FileConfigDTO<AccountDTO> csvInputConfig = FileConfigDTO.<AccountDTO>builder()
                                                                            .path(inputPath)
                                                                            .splitter(",")
                                                                            .classType(AccountDTO.class)
                                                                            .build();


        FileConfigDTO<AccountProcessedDTO> csvOutputConfig = FileConfigDTO.<AccountProcessedDTO>builder()
                                                                            .path(outputPath)
                                                                            .splitter(",")
                                                                            .classType(AccountProcessedDTO.class)
                                                                            .build();

        return IOFileConfigDTO.<AccountDTO,AccountProcessedDTO>builder()
                .inputConfig(csvInputConfig)
                .outputConfig(csvOutputConfig)
                .build();
    }
}
