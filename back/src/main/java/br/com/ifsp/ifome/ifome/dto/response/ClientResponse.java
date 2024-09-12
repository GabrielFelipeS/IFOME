package br.com.ifsp.ifome.ifome.dto.response;

import java.time.LocalDate;

public record ClientResponse(
    Long id,
    String email,
    LocalDate dateOfBirth,
    String cpf,
    String typeResidence,
    String cep,
    String address,
    String paymentMethods
) { }
