package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.CustomerOrder;

import java.util.List;

public record PusherDeliveryOrderResponse(
    Long customerOrderId,
    String nameClient,
    String phoneClient,
    AddressResponse addressClient,
    AddressResponse addressRestaurant,
    Double totalPrice,
    String expectedTime,
    Double deliveryCost,
    List<OrderInfoDeliveryResponse> status,
    List<OrderItemDeliveryResponse> orderItens
) {

    public static PusherDeliveryOrderResponse from(CustomerOrder customerOrder, DeliveryOrderResponse deliveryOrderResponse) {
        return new PusherDeliveryOrderResponse(
            customerOrder.getId(),
            deliveryOrderResponse.nameClient(),
            deliveryOrderResponse.phoneClient(),
            deliveryOrderResponse.addressClient(),
            deliveryOrderResponse.addressRestaurant(),
            deliveryOrderResponse.totalPrice(),
            deliveryOrderResponse.getOrderTime(),
            customerOrder.deliveryCost(),
            customerOrder.getOrderInfoDelivery().stream().map(OrderInfoDeliveryResponse::from).toList(),
            customerOrder.getOrderItems().stream().map(OrderItemDeliveryResponse::new).toList()
        );
    }
}