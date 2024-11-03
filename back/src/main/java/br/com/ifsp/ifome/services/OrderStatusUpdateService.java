package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
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

    public SseEmitter getEmitter(CustomerOrder customerOrder) {
        return this.addOrder(customerOrder);
    }

    private List<SseEmitter> getEmitters(CustomerOrder customerOrder) {
        return sseEmitterHashMap.getOrDefault(customerOrder.getId(), Collections.emptyList());
    }

    public SseEmitter addOrder(CustomerOrder customerOrder) {
        System.err.println("CRIANDO EMITTER");
        SseEmitter emitter = new SseEmitter(3_600_000L );

        System.err.println("ONTIMEOUT EMITTER");
        emitter.onTimeout(() -> {
            System.err.println("AQUI");
            sseEmitterHashMap.remove(customerOrder.getId());
        });

        System.err.println("ONCOMPLETION EMITTER");
        emitter.onCompletion(() ->
          this.remove(emitter, customerOrder)
        );

        System.err.println("ONERROR EMITTER");
        emitter.onError((e) -> {
            System.err.println("Emmiter Erro: " + e.getMessage());
            this.remove(emitter, customerOrder);
        });

        System.err.println("ADICIONANDO EMITTER");

        var newEmitter = new CopyOnWriteArrayList<>(List.of(emitter));

        if(sseEmitterHashMap.containsKey(customerOrder.getId())) {
            sseEmitterHashMap.merge(customerOrder.getId(), newEmitter, (currentValue, newValue) -> {
                System.err.println(currentValue);
                System.err.println(newValue);
                currentValue.addAll(newValue);
                return currentValue;
            });
        } else {
            sseEmitterHashMap.put(customerOrder.getId(), newEmitter);
        }

        System.err.println(customerOrder.getId());

//        this.send(emitter, customerOrder);

        return emitter;
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
    public void updateStatusOrderToRestaurant(CustomerOrder customerOrder, OrderDeliveryStatus orderInfoDelivery) {
        DeliveryOrderResponse deliveryOrderResponse = new DeliveryOrderResponse(customerOrder);

        System.err.println(deliveryOrderResponse.addressRestaurant());
        System.err.println("ANTES DO PUSHER");
        pusher.trigger(
            "pedidos",
            "entregador_" + customerOrder.getDeliveryPerson().getId().toString(),
            Map.of(
                "nameClient", deliveryOrderResponse.nameClient(),
                "phoneClient", deliveryOrderResponse.phoneClient(),
                "addressClient", deliveryOrderResponse.addressClient(),
                "addressRestaurant", deliveryOrderResponse.addressRestaurant(),
                "totalPrice", deliveryOrderResponse.totalPrice(),
                "expectedTime", deliveryOrderResponse.getOrderTime(),
                  "deliveryCost", customerOrder.deliveryCost(),
                "status", orderInfoDelivery.toString().replaceAll("_", "").toLowerCase()
            )
        );
        System.err.println("DEPOIS DO PUSHER");
    }

    private void remove(SseEmitter emitter, CustomerOrder customerOrder) {
        var emitters = sseEmitterHashMap.get(customerOrder.getId());
        System.err.println(emitters);
        System.err.println(sseEmitterHashMap);
        emitters.remove(emitter);
        System.err.println(emitters);
        sseEmitterHashMap.put(customerOrder.getId(), emitters);
        System.err.println(sseEmitterHashMap);

        System.err.println("EXECUTOU E NÂO DEVIA");
    }
}
