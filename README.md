# Sincronização de Contas

Este projeto realiza a sincronização de dados de contas a partir de arquivos CSV, processando-os de maneira paralela para otimizar o desempenho e a eficiência. Utiliza a biblioteca `picocli` para gerenciamento de argumentos de linha de comando e `OpenCSV` para processamento de arquivos CSV.

## Funcionalidades

- **Processamento Paralelo**: A aplicação utiliza múltiplas threads para processar registros em paralelo, o que melhora a eficiência e reduz o tempo de processamento.
- **Leitura e Escrita de CSV**: Utiliza a biblioteca `OpenCSV` para ler e escrever arquivos CSV.
- **Configuração Flexível**: Permite configuração de parâmetros de execução através de argumentos de linha de comando.

## Estrutura de Diretórios

- **src/main/java/**: Código-fonte da aplicação.
   - **br.com.sicredi.sincronizacao.application.usecases**: Contém casos de uso relacionados à sincronização de contas.
   - **br.com.sicredi.sincronizacao.infra**: Implementações de infraestrutura, incluindo validação de arquivos e processamento CSV.
   - **br.com.sicredi.sincronizacao.domain.dto**: Objetos de transferência de dados (DTOs).
   - **br.com.sicredi.sincronizacao.mappers**: Mapeadores para converter entre diferentes representações de dados.
- **src/main/resources/**: Arquivos de configuração e recursos da aplicação.

## Configuração

### Argumentos de Linha de Comando

A aplicação utiliza a biblioteca `picocli` para gerenciar argumentos de linha de comando. Os seguintes parâmetros podem ser utilizados:

1. **`--minNumThreads`** (opcional)
   - **Descrição**: Define o número mínimo de threads que o pool de threads deve manter ativo.
   - **Uso**: `--minNumThreads=<número>`
   - **Valor Padrão**: `-1` (valor padrão definido no código se não especificado).
   - **Exemplo**: `--minNumThreads=4`

2. **`--maxNumThreads`** (opcional)
   - **Descrição**: Define o número máximo de threads que o pool de threads pode criar.
   - **Uso**: `--maxNumThreads=<número>`
   - **Valor Padrão**: `-1` (valor padrão definido no código se não especificado).
   - **Exemplo**: `--maxNumThreads=10`

3. **`--keepAliveTime`** (opcional)
   - **Descrição**: Especifica o tempo que as threads ociosas devem permanecer vivas antes de serem encerradas, medido em segundos.
   - **Uso**: `--keepAliveTime=<tempo_em_segundos>`
   - **Valor Padrão**: `-1` (valor padrão definido no código se não especificado).
   - **Exemplo**: `--keepAliveTime=60`

4. **`filePaths`** (obrigatório)
   - **Descrição**: Caminhos dos arquivos CSV a serem processados pela aplicação.
   - **Uso**: `<caminho_do_arquivo_1> <caminho_do_arquivo_2> ...`
   - **Exemplo**: `file1.csv file2.csv`

### Exemplo de Uso

```bash
java -jar build/libs/sincronizacao-1.0.0.jar --minNumThreads=4 --maxNumThreads=10 --keepAliveTime=60 file1.csv file2.csv
```

## Processamento Paralelo

A aplicação utiliza processamento paralelo para otimizar a execução de tarefas. O processamento paralelo permite que múltiplos registros sejam processados simultaneamente, o que reduz significativamente o tempo total de processamento para grandes volumes de dados. Os parâmetros `--minNumThreads`, `--maxNumThreads` e `--keepAliveTime` permitem a configuração dinâmica do pool de threads:

- **Número Mínimo de Threads** (`--minNumThreads`): Garante que pelo menos um número mínimo de threads esteja disponível para o processamento.
- **Número Máximo de Threads** (`--maxNumThreads`): Limita o número máximo de threads simultâneas, ajudando a evitar a sobrecarga do sistema.
- **Tempo de Inatividade das Threads** (`--keepAliveTime`): Define quanto tempo uma thread ociosa deve esperar antes de ser encerrada, permitindo uma gestão eficiente dos recursos de threads.

## Explicação dos Arquivos

- **SyncFileUseCase**: Contém a lógica para sincronização de contas. Valida os arquivos CSV e processa cada registro usando o `FileProcessorGateway`.

- **SyncCommandLineRunner**: Configura e executa a aplicação com base nos argumentos de linha de comando fornecidos. Verifica se os caminhos dos arquivos CSV são fornecidos e chama o caso de uso `SyncFileUseCase`.

- **BancoCentralAccountService**: Implementa o gateway para comunicação com o Banco Central para atualizar os dados da conta. Realiza validação e simula a atualização da conta.

- **OpenCSVProcessor**: Processador genérico para arquivos CSV que utiliza múltiplas threads para melhorar o desempenho. Lê registros do CSV, aplica a função de processamento e escreve os resultados em um arquivo CSV de saída.

- **CSVThreadPoolConfig**: Configura o pool de threads usado para o processamento paralelo dos registros CSV. Permite a definição de número mínimo e máximo de threads, além do tempo de inatividade.

- **CSVMapperHelper**: Auxilia na conversão entre registros CSV e beans Java. Mapeia registros CSV para objetos Java e vice-versa.

- **CSVReaderHelper**: Fornece utilitários para criar leitores CSV a partir de arquivos.

- **CSVWriterHelper**: Fornece utilitários para criar escritores CSV e configurar cabeçalhos de arquivo de saída.

- **CSVFileValidator**: Valida arquivos CSV verificando se o arquivo existe e se tem a extensão CSV correta.

- **AccountFileConfigMapperImpl**: Mapeia configurações de arquivos CSV para objetos de configuração de entrada e saída.

## Dependências

- **picocli**: Para gerenciamento de argumentos de linha de comando.
- **OpenCSV**: Para leitura e escrita de arquivos CSV.

## Configuração do Gradle

### Adicione as dependências no seu arquivo `build.gradle`:

```groovy
dependencies {
   implementation 'info.picocli:picocli:4.8.1'
   implementation 'com.opencsv:opencsv:5.5.2'
   // Outras dependências necessárias
}
```

### Construindo o JAR Executável

Para construir um JAR executável, use o comando Gradle:

```bash
./gradlew clean build
```

O JAR será gerado em `build/libs/`.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para enviar pull requests e relatar problemas.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
