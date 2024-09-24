package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OpeningHoursRequest(
        @NotBlank
        String dayOfTheWeek,
        @NotBlank
        String opening,
        @NotBlank
        String closing


) {
}
