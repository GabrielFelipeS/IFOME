package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Dish dish,
        Integer quantity,
        Double price,
        LocalDateTime orderDate
) {
    public OrderResponse (Order order){
        this(
                order.getId(),
                order.getDish(),
                order.getQuantity(),
                order.getPrice(),
                order.getOrderDate()
        );
    }
}
