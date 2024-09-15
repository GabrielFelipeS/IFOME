package br.com.ifsp.ifome.dto.response;

public record LoginResponse(
    Object user,
    String token
) { }
