package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
    @NotBlank(message = "A mensagem não pode ser vazia")
    String content
) { }
