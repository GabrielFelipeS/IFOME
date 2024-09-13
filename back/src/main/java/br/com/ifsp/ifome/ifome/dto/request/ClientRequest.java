package br.com.ifsp.ifome.ifome.dto.request;

import br.com.ifsp.ifome.ifome.validation.anotations.NotRegisteredEmail;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ClientRequest (
        @NotBlank(message = "E-mail invalido")
        @NotRegisteredEmail
        String email,
        String password,
        String confirmationPassword,
        LocalDate dateOfBirth,
        String cpf,
        String typeResidence,
        String cep,
        String address,
        String paymentMethods
) { }
