package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.entities.OrderStatus;

public record UpdateOrderStatusRequest(
        Long customerOrderId,
        OrderStatus newStatus
) {

}
