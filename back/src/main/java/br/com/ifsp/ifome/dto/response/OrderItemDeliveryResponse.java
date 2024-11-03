package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.OrderItem;

public record OrderItemDeliveryResponse(
        Long id,
       String dishName,
        Integer quantity,
        Double orderPrice
) {
    public OrderItemDeliveryResponse(OrderItem orderItem){
        this(
                orderItem.getId(),
                orderItem.getDish().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }
}
