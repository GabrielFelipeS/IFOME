package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.CEP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(

    @CEP
    @NotBlank(message = "Cep é obrigatório")
    String cep,

    @NotBlank(message = "Nome do endereço é obrigatório")
    String nameAddress,

    @NotBlank(message = "Bairro é obrigatório")
    String neighborhood,

    @NotBlank(message = "Cidade é obrigatório")
    String city,

    @NotBlank(message = "Estado é obrigatório")
    String state,

    @NotBlank(message = "Address é obrigatório")
    String address,

    @NotBlank(message = "Complemento é obrigatório")
    String complement,

    @NotBlank(message = "Número é obrigatório")
    @Pattern(regexp = "\\d{1,}")
    String number,

    @NotBlank(message = "Tipo de residência é obrigatório")
    @Pattern(regexp = "(casa|apartamento|condom[ií]nio)")
    String typeResidence,

    String details
) { }
