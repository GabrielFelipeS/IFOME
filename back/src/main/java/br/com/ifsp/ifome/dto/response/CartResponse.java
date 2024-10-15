package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;

import java.util.List;

public record CartResponse(
        Long Id,
        Client client,
        Restaurant restaurant,
        DeliveryPerson deliveryPerson,
        List<Order> orders,
        Double deliveryFee
) {
    public CartResponse (Cart cart){
        this(
                cart.getId(),
                cart.getClient(),
                cart.getRestaurant(),
                cart.getDeliveryPerson(),
                cart.getOrders(),
                cart.getDeliveryFee()
        );
    }
}
