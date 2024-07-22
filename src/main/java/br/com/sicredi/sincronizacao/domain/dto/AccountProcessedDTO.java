package br.com.sicredi.sincronizacao.domain.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AccountProcessedDTO extends AccountDTO{

    @CsvBindByName(column = "status")
    @NonNull
    private Boolean status;

    public AccountProcessedDTO(String agencia, String conta, Double saldo, @NonNull Boolean status) {
        super(agencia, conta, saldo);
        this.status = status;
    }
}
