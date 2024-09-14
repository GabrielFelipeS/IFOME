package br.com.ifsp.ifome.dto.request;

public record AddressRequest(
    String cep,
    String neighborhood,
    String city,
    String state,
    String address,
    String zipCode,
    String complement,

    String typeResidence,
    String number,
    String walk,
    String details
) { }
