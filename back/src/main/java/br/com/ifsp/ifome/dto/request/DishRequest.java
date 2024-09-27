package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DishRequest(
        @NotBlank (message = "O nome do prato é obrigatório")
        String name,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "Insira um valor de preço válido.")
        Double price,

        @NotBlank(message = "A categoria do prato é obrigatória")
        String dishCategory,

        String dishImage,

        @NotBlank(message = "A disponibilidade é obrigatória")
        String availability
) {}
