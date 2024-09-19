package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.BankAccountRequest;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Embeddable
public class BankAccount {
    private String bank;
    private String agency;
    private String account;

    public BankAccount() {}

    public BankAccount(BankAccountRequest bankAccountRequest) {
        this.bank = bankAccountRequest.bank();
        this.agency = bankAccountRequest.agency();
        this.account = bankAccountRequest.account();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

