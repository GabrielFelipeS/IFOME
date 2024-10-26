package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.CustomerOrder;

import java.time.LocalDateTime;

public record CustomerOrderResponse(
        Long orderId,
        Double orderPrice,
        String status,
        String paymentStatus,
        LocalDateTime orderDate
) {
    public CustomerOrderResponse(CustomerOrder customerOrder){
        this(
                customerOrder.getId(),
                customerOrder.getOrderPrice(),
                customerOrder.getStatus().toString(),
                customerOrder.getPaymentStatus(),
                customerOrder.getOrderDate()
        );
    }
}
