package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Order;

public record OrderResponse(
        Long id,
        Dish dish,
        Integer quantity,
        Double price
) {
    public OrderResponse (Order order){
        this(
                order.getId(),
                order.getDish(),
                order.getQuantity(),
                order.getPrice()
        );
    }
}
