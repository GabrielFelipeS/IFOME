package br.com.ifsp.ifome.ifome.dto.request;

import java.time.LocalDateTime;

public record ClientRequest (
        String email,
        String password,
        String confirmationPassword,
        LocalDateTime date_of_birth,
        String cpf,
        String type_residence,
        String cep,
        String endereco,
        String payment_methods
) { }
