package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record DishRequest(
        @NotBlank (message = "O nome do prato é obrigatório")
        String name,

        @NotBlank(message = "A descrição do prato é obrigatória")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve conter apenas valores númericos e positivos")
        Double price,

        @NotBlank(message = "A categoria do prato é obrigatória")
        String dishCategory,

        @Pattern(regexp = "(?i)(Disponível|Indisponível)", message = "A disponibilidade deve ser 'Disponível' ou 'Indisponível'")
        @NotBlank(message = "A disponibilidade é obrigatória")
        String availability
) {}
