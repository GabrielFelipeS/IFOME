package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClientLoginRequest(
    @NotBlank
    String email,
    @NotBlank
    String password) {
}
