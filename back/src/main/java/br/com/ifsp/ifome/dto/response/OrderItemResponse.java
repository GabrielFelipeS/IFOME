package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;

public record OrderItemResponse(
        Long id,
        Dish dish,
        Integer quantity,
        Double orderPrice
) {
    public OrderItemResponse(OrderItem orderItem){
        this(
                orderItem.getId(),
                orderItem.getDish(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }
}
