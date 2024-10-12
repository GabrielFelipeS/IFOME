package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.BankAccountRequest;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class BankAccount {
    private String bank;
    private String agency;
    private String account;

    public BankAccount(BankAccountRequest bankAccountRequest) {
        this.bank = bankAccountRequest.bank();
        this.agency = bankAccountRequest.agency();
        this.account = bankAccountRequest.account();
    }
}

