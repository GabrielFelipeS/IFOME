package br.com.ifsp.ifome.entities;

import jakarta.persistence.Embeddable;
@Embeddable
public class BankAccount {
    private String bank;
    private String agency;
    private String account;

    public BankAccount() {}

    public BankAccount(String bank, String agency, String account) {
        this.bank = bank;
        this.agency = agency;
        this.account = account;
    }

    // Getters e Setters
}

