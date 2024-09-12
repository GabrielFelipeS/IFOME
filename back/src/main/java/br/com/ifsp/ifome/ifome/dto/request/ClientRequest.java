package br.com.ifsp.ifome.ifome.dto.request;

import java.time.LocalDate;

public record ClientRequest (
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
