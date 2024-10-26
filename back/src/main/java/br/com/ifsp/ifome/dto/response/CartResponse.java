package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;

import java.util.List;

public record CartResponse(
        List<OrderItem> orderItems,
        Double totalprice
) {
    public CartResponse (Cart cart){
        this(
                cart.getOrderItems(),
                cart.totalPrice()
        );
    }
}
