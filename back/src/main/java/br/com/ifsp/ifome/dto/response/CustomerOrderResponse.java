package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderInfo;
import br.com.ifsp.ifome.entities.OrderItem;

import java.util.List;

public record CustomerOrderResponse(
    Long orderId,
    String name,
    Address address,
    List<OrderItem> orderItems,
    Double orderPrice,
    List<OrderInfo> orderInfo,
    String paymentStatus,
    String orderDate
) {
    public CustomerOrderResponse(CustomerOrder customerOrder){
        this(
                customerOrder.getId(),
                customerOrder.getClientName(),
                customerOrder.getAddress(),
                customerOrder.getCart().getOrderItems(),
                customerOrder.getOrderPrice(),
                customerOrder.getOrderInfo(),
                customerOrder.getPaymentStatus(),
                customerOrder.getOrderDate()
        );
    }
}
