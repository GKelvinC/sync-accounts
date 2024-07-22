package br.com.sicredi.sincronizacao.infra;

import br.com.sicredi.sincronizacao.application.gateways.SyncGateway;
import br.com.sicredi.sincronizacao.domain.dto.SyncAccountsParametersDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncCommandLineRunner implements CommandLineRunner {

    private final SyncGateway syncGateway;

    @Override
    public void run(String... args) {
        SyncAccountsParametersDTO config = new SyncAccountsParametersDTO();
        CommandLine cmd = new CommandLine(config);

        try {
            cmd.parseArgs(args);
            if (config.getFilePaths().length > 0) {
                syncGateway.syncAccounts(config);
            } else {
                log.error("Forneça o caminho para o arquivo CSV como argumento.");
                System.exit(0);
            }
        } catch (Exception e) {
            log.error("Erro ao processar argumentos: ", e);
            System.exit(1);
        }
    }
}


