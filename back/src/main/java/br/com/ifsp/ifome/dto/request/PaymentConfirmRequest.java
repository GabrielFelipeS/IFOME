package br.com.ifsp.ifome.dto.request;

public record PaymentConfirmRequest(
    Long orderId,
    String paymentMethodId
) {
}
