package br.com.sicredi.sincronizacao.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

@Slf4j
public class AccountCsvGenerator {

    public static final String FILE_PATH = "./src/main/resources/csvProcessed/DataMockGenerated.csv";
    public static final int NUM_LINES = 10_000_000; // 10 milhões de linhas
    private static final double CHANCE_EMPTY_VALUE = 0.20;
    private static final Random RANDOM = new Random();
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static void main(String[] args) {
        try {
            generateCsv();
        } catch (IOException e) {
            log.error("Erro durante a criação do arquivo CSV: ", e);
        }
    }

    private static void generateCsv() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("agencia,conta,saldo\n");
            for (int i = 1; i <= NUM_LINES; i++) {
                String line = generateLine();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static String generateLine() {
        String agencia = generateAgencia();
        String conta = generateConta();
        String saldo = generateSaldo().replace(",",".");
        return String.format("%s,%s,%s",agencia, conta, saldo);
    }


    private static String generateConta() {
        if (RANDOM.nextDouble() <= CHANCE_EMPTY_VALUE) {
            return "";
        }
        int part1 = RANDOM.nextInt(100000);
        int part2 = RANDOM.nextInt(10);
        return String.format("%05d-%d", part1, part2);
    }

    public static String generateAgencia() {
        if (RANDOM.nextDouble() <= CHANCE_EMPTY_VALUE) {
            return "";
        }
        int number = RANDOM.nextInt(9000) + 1000;
        return String.format("%04d", number);
    }

    public static String generateSaldo() {
        if (RANDOM.nextDouble() <= CHANCE_EMPTY_VALUE) {
            return "";
        }
        int integerPart = RANDOM.nextInt(1000);
        int decimalPart = RANDOM.nextInt(100);
        double number = integerPart + decimalPart / 100.0;
        return DECIMAL_FORMAT.format(number);
    }
}