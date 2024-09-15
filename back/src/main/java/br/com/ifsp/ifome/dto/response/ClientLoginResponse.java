package br.com.ifsp.ifome.dto.response;

public record ClientLoginResponse(
    ClientResponse user,
    String token
) { }
