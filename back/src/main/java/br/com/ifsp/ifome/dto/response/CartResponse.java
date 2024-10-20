package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;

import java.util.List;

public record CartResponse(
        Client client,
        List<OrderItem> orderItems,
        Double totalprice
) {
    public CartResponse (Cart cart){
        this(
                cart.getClient(),
                cart.getOrderItems(),
                cart.totalPrice()
        );
    }
}
