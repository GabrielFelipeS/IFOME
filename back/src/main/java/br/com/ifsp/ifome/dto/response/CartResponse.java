package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;

import java.util.List;

public record CartResponse(
        Long Id,
        Client client,
        List<OrderItem> orderItems
) {
    public CartResponse (Cart cart){
        this(
                cart.getId(),
                cart.getClient(),
                cart.getOrderItems()
        );
    }
}
