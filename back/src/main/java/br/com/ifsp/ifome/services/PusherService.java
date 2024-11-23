package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.LoggingAspect;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.OrderInfoDeliveryResponse;
import br.com.ifsp.ifome.dto.response.OrderItemDeliveryResponse;
import br.com.ifsp.ifome.entities.*;
import com.pusher.rest.Pusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class PusherService {
    private static final Logger logger = LoggerFactory.getLogger(PusherService.class);

    private final Pusher pusher;

    public PusherService(Pusher pusher) {
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
        String infos = String.format(
            "Channel: order-channel \nEventName: order-status-updated_%s\nOrderId: %d\nStatus: %s\nPosition: %d\nTime: %s",
            customerOrder.getId().toString(),
            customerOrder.getId(),
            orderClientStatus.toString(),
            customerOrder.getOrderStatusId(),
            customerOrder.getOrderDateTimeToTimestamp()
        );

        logger.info(infos);

        pusher.trigger(
            "order-channel",
            "order-status-updated_" + customerOrder.getId().toString(),
            Map.of(
                "orderId", customerOrder.getId(),
                "status", orderClientStatus.toString(),
                "position", customerOrder.getOrderStatusId(),
                "time", customerOrder.getOrderDateTimeToTimestamp()
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

    /**
     * Envia uma atualização para o entregador, com status vazio, usado para atualizar a tela do entregador
     *
     * @param customerOrderId Id do pedido
     */
    @Async
    public void updateStatusCanceledToDeliverer(Long customerOrderId) {
        String timestamp = Timestamp.from(Instant.now()).toString();

        Map<String, Object> data = Map.of(
            "status", List.of(new OrderInfoDeliveryResponse(customerOrderId, "CANCELADO", timestamp))
        );

        pusher.trigger(
            "pedidos",
            "entregador_" + customerOrderId,
            data
        );
    }

    /**
     *
     * @param message Informações da mensagem
     * @param chat Chat da mensagem vai ser enviada
     */
    @Async
    public void addMessageInChat(ClientDeliveryChat chat, Message message) {
        this.addMessage(message, chat, "client-delivery-chat");
    }

    /**
     *
     * @param message Informações da mensagem
     * @param chat Chat da mensagem vai ser enviada
     */
    @Async
    public void addMessageInChat(ClientRestaurantChat chat, Message message) {
        this.addMessage(message, chat, "client-restaurant-chat");
    }

    /**
     *
     * @param message Informações da mensagem
     * @param chat Chat da mensagem vai ser enviada
     */
    @Async
    public void addMessageInChat(RestaurantDeliveryChat chat, Message message) {
        this.addMessage(message, chat, "restaurant-delivery-chat");
    }

    /**
     *
     * @param message Informações da mensagem
     * @param chat Chat da mensagem vai ser enviada
     * @param event Canal da mensagem
     */
    private void addMessage(Message message, Chat chat, String event) {
        Map<String, Object> data = Map.of(
            "content", message.getContent(),
            "senderType", message.getSenderType(),
            "createdAt", message.getCreatedAtTimestamp()
        );

        System.err.println("OrderId: " + chat.getCustomerOrderId());
        System.err.println("Content: " + data.get("content"));
        System.err.println("senderType: " + data.get("senderType"));
        System.err.println("createdAt: " + data.get("createdAt"));

        pusher.trigger(
            "chat_" + chat.getCustomerOrderId(),
            event,
            data
        );
    }
}
