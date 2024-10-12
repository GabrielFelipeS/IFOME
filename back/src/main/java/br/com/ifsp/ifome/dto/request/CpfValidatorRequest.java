package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CpfValidatorRequest(
    @CPF(message = "O campo \"CPF\" deve estar no formato: XXX.XXX.XXX-XX, valor atual: {validatedValue}")
    @NotBlank(message = "O campo \"CPF\" é obrigatório")
    String cpf
) {
}
