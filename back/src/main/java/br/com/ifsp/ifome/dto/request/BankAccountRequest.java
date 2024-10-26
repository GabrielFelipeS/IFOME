package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BankAccountRequest(
        @NotBlank(message = "O campo \"Banco\" é obrigatório")
        String bank,

        @NotBlank(message = "O campo \"Agência\" é obrigatório")
        String agency,

        @NotBlank(message = "O campo \"Conta\" é obrigatório")
        String account
)  {
}
