package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.CustomerOrder;

public record CustomerOrderRequest(
    Long id,
    Double orderPrice,
    String paymentStatus,
    String orderDate
) {
    public static CustomerOrderRequest from(CustomerOrder customerOrder) {
        return new CustomerOrderRequest(
            customerOrder.getId(),
            customerOrder.getOrderPrice(),
            customerOrder.getPaymentStatus(),
            customerOrder.getOrderDateTimeToTimestamp()
        );
    }
}
