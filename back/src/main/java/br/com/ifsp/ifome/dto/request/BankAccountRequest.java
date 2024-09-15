package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.entities.BankAccount;

public record BankAccountRequest(

        String bank,

        String agency,

        String account
)  {
}
