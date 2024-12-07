package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotNull;

public record PaymentOrderRequest(
    @NotNull(message = "O id do pedido não pode ser nulo")
    Long orderId
) {
}
