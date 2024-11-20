package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.OrderInfoDeliveryResponse;
import br.com.ifsp.ifome.dto.response.OrderItemDeliveryResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderClientStatus;
import com.pusher.rest.Pusher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class OrderStatusUpdateService {
    private final Pusher pusher;

    public OrderStatusUpdateService(Pusher pusher) {
        this.pusher = pusher;
    }

    /**
     * Atualiza as informações de status do pedido do cliente para todos os interesados do canal {@code order-channel}, inscritos no evento {@code order-status-updated_{idCustomerOrder}}
     *
     * @param customerOrder Pedido do cliente
     * @param orderClientStatus Status do pedido do cliente
     */
    @Async
    public void updateStatusOrderToClient(CustomerOrder customerOrder, OrderClientStatus orderClientStatus)  {
        pusher.trigger(
            "order-channel",
            "order-status-updated_" + customerOrder.getId().toString(),
            Map.of(
                "orderId", customerOrder.getId(),
                "status", orderClientStatus.toString(),
                "position", customerOrder.getOrderStatusId(),
                "time", customerOrder.getOrderDateTime()
            )
        );
    }

    /**
     * Atualiza as informações de status do pedido do cliente para todos os interesados do canal {@code pedidos}, inscritos no evento {@code entregador_{id}}
     *
     * @param customerOrder Pedido do cliente
     */
    @Async
    public void updateStatusOrderToRestaurant(CustomerOrder customerOrder) {
        DeliveryOrderResponse deliveryOrderResponse = new DeliveryOrderResponse(customerOrder);

        var orderInfoDeliveryList = customerOrder
                                .getOrderInfoDelivery()
                                .stream()
                                .map(OrderInfoDeliveryResponse::from)
                                .toList();

        Map<String, Object> data = Map.of(
            "customerOrderId", customerOrder.getId(),
            "nameClient", deliveryOrderResponse.nameClient(),
            "phoneClient", deliveryOrderResponse.phoneClient(),
            "addressClient", deliveryOrderResponse.addressClient(),
            "addressRestaurant", deliveryOrderResponse.addressRestaurant(),
            "totalPrice", deliveryOrderResponse.totalPrice(),
            "expectedTime", deliveryOrderResponse.getOrderTime(),
            "deliveryCost", customerOrder.deliveryCost(),
            "status", orderInfoDeliveryList,
            "orderItems", customerOrder.getOrderItems().stream().map(OrderItemDeliveryResponse::new).toList()
            );

        pusher.trigger(
            "pedidos",
            "entregador_" + customerOrder.getDeliveryPerson().getId().toString(),
           data
        );
    }

}
