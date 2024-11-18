package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.OrderInfoDelivery;

import java.time.format.DateTimeFormatter;

public record OrderInfoDeliveryResponse (
    Long id,
    String orderDeliveryStatus,
    String localDateTime
) {

    public static OrderInfoDeliveryResponse from(OrderInfoDelivery orderInfoDelivery) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_TIME;

        return new OrderInfoDeliveryResponse(
            orderInfoDelivery.getId(),
            orderInfoDelivery.getOrderDeliveryStatus().toString(),
            orderInfoDelivery.getLocalDateTime().format(dateTimeFormatter)
        );
    }
}
