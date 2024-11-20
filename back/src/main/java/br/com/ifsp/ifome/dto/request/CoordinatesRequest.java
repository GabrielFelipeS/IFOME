package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CoordinatesRequest(
    @NotBlank
    String latitude,
    @NotBlank
    String longitude
) { }
