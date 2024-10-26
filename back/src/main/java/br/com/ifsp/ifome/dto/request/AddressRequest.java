package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.CEP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(

    @CEP
    @NotBlank(message = "O campo \"Cep\" é obrigatório")
    String cep,

    @NotBlank(message = "Nome do endereço é obrigatório")
    String nameAddress,

    @NotBlank(message = "O campo \"Bairro\" é obrigatório")
    String neighborhood,

    @NotBlank(message = "O campo \"Cidade\" é obrigatório")
    String city,

    @NotBlank(message = "O campo \"Estado\" é obrigatório")
    String state,

    @NotBlank(message = "O campo \"Address\" é obrigatório")
    String address,

    @NotBlank(message = "O campo \"Complemento\" é obrigatório")
    String complement,

    @NotBlank(message = "O campo \"Número\" é obrigatório")
    @Pattern(message = "O número da residência deve conter apenas dígitos numéricos e incluir pelo menos um algarismo",regexp = "\\d{1,}")
    String number,

    @NotBlank(message = "O campo \"Tipo de residência\" é obrigatório")
    @Pattern(message = "Os tipos de residência permitido são: casa, apartamento e condominío", regexp = "(casa|apartamento|condom[ií]nio)")
    String typeResidence,

    String details
) { }
