package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.CEP;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
    @NotBlank
    @CEP
    String cep,

    @NotBlank
    String neighborhood,

    @NotBlank
    String city,

    @NotBlank
    String state,

    @NotBlank
    String address,

    @NotBlank
    String zipCode,

    @NotBlank
    String complement,

    @NotBlank
    String typeResidence,

    @NotBlank
    String number,

    String details
) { }
