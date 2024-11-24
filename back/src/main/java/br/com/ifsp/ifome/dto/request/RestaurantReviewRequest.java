package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantReviewRequest(
        //@NotNull(message = "A nota é obrigatória.")
        @Min(value = 1, message = "A nota mínima é 1.")
        @Max(value = 5, message = "A nota máxima é 5.")
        double stars,

        @Size(max = 250, message = "O comentário não pode ter mais de 250 caracteres.")
        String comment
) {
}
