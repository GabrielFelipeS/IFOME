package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    String email,
    @NotBlank
    String password) {
}
