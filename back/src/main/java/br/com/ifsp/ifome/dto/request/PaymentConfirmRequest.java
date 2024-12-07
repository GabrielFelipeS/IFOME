package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentConfirmRequest(
    @NotNull(message = "O id do pedido não pode ser nulo")
    Long orderId,
    @NotBlank(message = "O id do metodo de pagamento")
    String paymentMethodId
) {
}
