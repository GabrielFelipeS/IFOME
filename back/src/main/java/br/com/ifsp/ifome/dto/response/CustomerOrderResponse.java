package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderInfo;
import br.com.ifsp.ifome.entities.OrderItem;

import java.util.List;

public record CustomerOrderResponse(
    Long orderId,
    Long restaurantId,
    String name,
    Address address,
    List<OrderItem> orderItems,
    Double orderPrice,
    List<OrderInfo> orderInfo,
    String paymentStatus,
    String orderDate,
    Double freight,
    Double totalPrice
) {
    public static CustomerOrderResponse from(CustomerOrder customerOrder){
        return new CustomerOrderResponse(
            customerOrder.getId(),
            customerOrder.getRestaurantId(),
            customerOrder.getClientName(),
            customerOrder.getClientAddress(),
            customerOrder.getOrderItems(),
            customerOrder.getOrderPrice(),
            customerOrder.getOrderInfo(),
            customerOrder.getPaymentStatus(),
            customerOrder.getOrderDateTimeToTimestamp(),
            customerOrder.getFreight(),
            customerOrder.totalPrice()
        );
    }
}
