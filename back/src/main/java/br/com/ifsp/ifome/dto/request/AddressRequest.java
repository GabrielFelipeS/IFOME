package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.CEP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(
    @NotBlank
    @CEP
    String cep,

    @NotBlank
    String nameAddress,

    @NotBlank
    String neighborhood,

    @NotBlank
    String city,

    @NotBlank
    String state,

    @NotBlank
    String address,

    @NotBlank
    String complement,

    @NotBlank
    @Pattern(regexp = "\\d{1,}")
    String number,

    @NotBlank
    @Pattern(regexp = "(casa|apartamento|condom[ií]nio)")
    String typeResidence,

    String details
) { }
