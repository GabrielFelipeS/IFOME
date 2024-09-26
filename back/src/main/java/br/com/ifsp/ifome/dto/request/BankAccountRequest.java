package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BankAccountRequest(
        @NotBlank(message = "Banco é obrigatório")
        String bank,

        @NotBlank(message = "Agência é obrigatório")
        String agency,

        @NotBlank(message = "Conta é obrigatório")
        String account
)  {
}
