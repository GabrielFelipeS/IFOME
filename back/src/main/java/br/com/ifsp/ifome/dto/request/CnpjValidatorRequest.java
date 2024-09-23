package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CnpjValidatorRequest(
    @CNPJ(message = "CNPJ inválido")
    @NotBlank(message = "CNPJ é obrigatório")
    String cnpj
) {
}
