package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;

public record OrderResponse(
        Long id,
        Dish dish,
        Integer quantity,
        Double price
) {
    public OrderResponse (OrderItem orderItem){
        this(
                orderItem.getId(),
                orderItem.getDish(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }
}
