package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CpfValidatorRequest(
    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    String cpf
) {
}
