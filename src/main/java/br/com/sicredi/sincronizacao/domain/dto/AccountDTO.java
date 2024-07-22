package br.com.sicredi.sincronizacao.domain.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @CsvBindByName(column = "agencia")
    private String agencia;
    @CsvBindByName(column = "conta")
    private String conta;
    @CsvBindByName(column = "saldo")
    private Double saldo;
}
