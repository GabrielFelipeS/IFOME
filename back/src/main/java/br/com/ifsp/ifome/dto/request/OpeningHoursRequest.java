package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OpeningHoursRequest(
        @NotBlank(message = "O campo \"dia da semana\" é obrigatório")
        @Pattern(regexp = "^(Segunda-feira|Terça-feira|Quarta-feira|Quinta-feira|Sexta-feira|Sábado|Domingo)$",
                message = "O dia da semana deve ser: Segunda-feira, Terça-feira, Quarta-feira, Quinta-feira, Sexta-feira, Sábado ou Domingo")
        String dayOfTheWeek,

        @NotBlank(message = "O campo \"Abertura\" é obrigatório")
        @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$",
                message = "Horário de abertura deve estar no formato HH:mm")
        String opening,

        @NotBlank(message = "O campo \"Fechamento\" é obrigatório")
        @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$",
                message = "Horário de fechamento deve estar no formato HH:mm")
        String closing


) {
        public OpeningHoursRequest {
                if (opening.compareTo(closing) >= 0) {
                        throw new IllegalArgumentException("Horário de abertura deve ser anterior ao de fechamento");
                }
        }
}
