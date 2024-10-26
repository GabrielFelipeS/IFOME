package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank(message = "O campo \"E-mail\" é obrigatório")
    @Email(message = "E-mail deve estar no formato: nome@dominio.com")
    String email,
    @NotBlank(message = "O campo \"Senha\" é obrigatório")
    String password) {
}
