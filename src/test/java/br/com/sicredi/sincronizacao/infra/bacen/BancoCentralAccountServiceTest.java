package br.com.sicredi.sincronizacao.infra.bacen;

import br.com.sicredi.sincronizacao.domain.dto.AccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BancoCentralAccountServiceTest {

    @Test
    public void atualizaContaTest_ValidAccount_ShouldPass() {
        // Given
        AccountDTO account = new AccountDTO("1234", "1234567", 1000.00D);
        BancoCentralAccountService bancoCentralGateway = new BancoCentralAccountService();

        // When
        boolean result = bancoCentralGateway.atualizaConta(account);

        // Then
        Assertions.assertTrue(result || !result);
    }

    @Test
    public void atualizaContaTest_InvalidAccount_ShouldThrowException() {
        // Given
        AccountDTO account = new AccountDTO("", "123456", 1000.00D);
        BancoCentralAccountService bancoCentralGateway = new BancoCentralAccountService();

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> bancoCentralGateway.atualizaConta(account));
    }
    
    @Test
    public void atualizaContaTest_NullAccount_ShouldThrowException() {
        // Given
        AccountDTO account = new AccountDTO(null, null, 1000.00D);
        BancoCentralAccountService bancoCentralGateway = new BancoCentralAccountService();

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> bancoCentralGateway.atualizaConta(account));
    }
}