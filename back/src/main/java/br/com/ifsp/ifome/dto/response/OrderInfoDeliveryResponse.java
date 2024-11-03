package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.OrderDeliveryStatus;
import br.com.ifsp.ifome.entities.OrderInfoDelivery;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record OrderInfoDeliveryResponse (
    Long id,
    OrderDeliveryStatus orderDeliveryStatus,
    String localDateTime
) {

    public static OrderInfoDeliveryResponse from(OrderInfoDelivery orderInfoDelivery) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_TIME;
        System.err.println("AQUI 1");
        return new OrderInfoDeliveryResponse(
            orderInfoDelivery.getId(),
            orderInfoDelivery.getOrderDeliveryStatus(),
            LocalDateTime.now().format(dateTimeFormatter)
        );
    }
}
