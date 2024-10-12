package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record DishRequest(
        @NotBlank (message = "O campo \"Nome do prato\" é obrigatório")
        String name,

        @NotBlank(message = "O campo \"Descrição\" é obrigatória")
        String description,

        @NotNull(message = "O campo \"preço\" é obrigatório")
        @Positive(message = "O preço deve conter apenas valores númericos e positivos")
        Double price,

        @NotBlank(message = "O campo \"Categoria\" é obrigatória")
        String dishCategory,

        @Pattern(regexp = "(?i)(Disponível|Indisponível)", message = "A disponibilidade deve ser 'Disponível' ou 'Indisponível'")
        @NotBlank(message = "O campo \"disponibilidade\" é obrigatória")
        String availability
) {}
