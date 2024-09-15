package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.entities.BankAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record BankAccountRequest(
        @NotBlank
        String bank,
        @NotBlank
        String agency,
        @NotBlank
        String account
)  {
}
