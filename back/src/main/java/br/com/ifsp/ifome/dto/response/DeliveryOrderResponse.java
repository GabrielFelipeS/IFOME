package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.CustomerOrder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record DeliveryOrderResponse(
    String nameClient,
    String phoneClient,
    Address addressClient,
    Address addressRestaurant,
    Double totalPrice,
    LocalTime expectedTime
) {
    public DeliveryOrderResponse(CustomerOrder customerOrder) {
        this(
          customerOrder.getClientName(),
          customerOrder.getClientPhone(),
          customerOrder.getClientAddress(),
          customerOrder.getRestaurantAddress(),
          customerOrder.getOrderPrice(),
          //TODO como devemos calcular a demora da rota
          LocalTime.now().plusMinutes(15)
        );
    }

    public String getOrderTime() {
        System.err.println("AQUI?");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_TIME;
        return this.expectedTime.format(dateTimeFormatter);
    }
}
