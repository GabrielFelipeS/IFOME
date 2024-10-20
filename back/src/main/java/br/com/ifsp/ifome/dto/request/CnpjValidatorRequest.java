package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CnpjValidatorRequest(
    @CNPJ(message = "O campo \"CNPJ\" deve estar no formato XX.XXX.XXX/XXXX-XX")
    @NotBlank(message = "O campo \"CNPJ\" é obrigatório")
    String cnpj
) {
}
