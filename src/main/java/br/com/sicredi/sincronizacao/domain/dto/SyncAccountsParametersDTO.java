package br.com.sicredi.sincronizacao.domain.dto;

import lombok.Data;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Data
public class SyncAccountsParametersDTO {

    @Option(names = {"--minNumThreads"}, description = "Número mínimo de threads", defaultValue = "-1")
    private int minNumThreads;

    @Option(names = {"--maxNumThreads"}, description = "Número máximo de threads", defaultValue = "-1")
    private int maxNumThreads;

    @Option(names = {"--keepAliveTime"}, description = "Tempo de inatividade das threads", defaultValue = "-1")
    private int keepAliveTime;

    @Parameters(description = "Caminhos dos arquivos CSV a serem processados")
    private String[] filePaths;
}
