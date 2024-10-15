package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.entities.Restaurant;

import java.util.List;

public record CartRequest(
        //Long id,
        Client client,
        Restaurant restaurant,
        DeliveryPerson deliveryPerson,
        List<OrderItem> orderItems,
        Double deliveryFee
) {
}
