package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.OrderInfoDeliveryResponse;
import br.com.ifsp.ifome.dto.response.OrderItemDeliveryResponse;
import br.com.ifsp.ifome.dto.response.OrderItemResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderClientStatus;
import br.com.ifsp.ifome.entities.OrderDeliveryStatus;
import com.pusher.rest.Pusher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderStatusUpdateService {
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, List<SseEmitter>> sseEmitterHashMap;
    private final Pusher pusher;

    public OrderStatusUpdateService(ApplicationEventPublisher eventPublisher, Pusher pusher) {
        this.eventPublisher = eventPublisher;
        this.pusher = pusher;
        this.sseEmitterHashMap = new ConcurrentHashMap<>();
    }

    @Async
    public void updateStatusOrderToClient(CustomerOrder customerOrder, OrderClientStatus orderClientStatus)  {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

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

    @Async
    public void updateStatusOrderToRestaurant(CustomerOrder customerOrder) {
        DeliveryOrderResponse deliveryOrderResponse = new DeliveryOrderResponse(customerOrder);

        System.err.println(deliveryOrderResponse.addressRestaurant());
        System.err.println("ANTES DO PUSHER");



        var orderInfoDeliveryList = customerOrder
                                .getOrderInfoDelivery()
                                .stream()
                                .map(OrderInfoDeliveryResponse::from)
                                .toList();

        orderInfoDeliveryList.forEach(System.err::println);

        Map<String, Object> map = Map.of(
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
           map
        );
        System.err.println("DEPOIS DO PUSHER");
    }

}
