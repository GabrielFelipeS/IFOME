package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
    @NotBlank
    @Email
    String email
) {
}
