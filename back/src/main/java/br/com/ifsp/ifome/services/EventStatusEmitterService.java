package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.events.PedidoStatusChangedEvent;
import com.pusher.rest.Pusher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.emitter.EmitterException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EventStatusEmitterService {
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, List<SseEmitter>> sseEmitterHashMap;
    private final Pusher pusher;

    public EventStatusEmitterService(ApplicationEventPublisher eventPublisher, Pusher pusher) {
        this.eventPublisher = eventPublisher;
        this.pusher = pusher;
        this.sseEmitterHashMap = new ConcurrentHashMap<>();
    }

    public SseEmitter getEmitter(CustomerOrder customerOrder) {
        return this.addEmitter(customerOrder);
    }

    private List<SseEmitter> getEmitters(CustomerOrder customerOrder) {
        return sseEmitterHashMap.getOrDefault(customerOrder.getId(), Collections.emptyList());
    }

    public SseEmitter addEmitter(CustomerOrder customerOrder) {
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

        this.send(emitter, customerOrder);

        return emitter;
    }

    @Async
    public void updateStatusEmitter(CustomerOrder customerOrder)  {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

//        String formatTime = customerOrder.getOrderDate().format(dateTimeFormatter);
        System.err.println(customerOrder.getOrderDate());
//        System.err.println(formatTime);
        System.err.println("ANTES DE EXECUTAR O PUSHER");
        pusher.trigger(
            customerOrder.getId().toString(),
            "update_status",
            Map.of(
                "status", customerOrder.getStatus(),
                "time", customerOrder.getOrderDate()
            )
        );
        System.err.println("DEPOIS DE EXECUTAR O PUSHER");
    }

    private void send(SseEmitter emitter, CustomerOrder customerOrder) {
        try {
            System.err.println(emitter);
            System.err.println("ANTES DE ENVIAR MENSAGEM EMITTER");
            emitter.send(customerOrder.getStatusMessage());
            System.err.println("DEPOIS DE ENVIAR MENSAGEM EMITTER");
        } catch (IOException e) {
          //  System.err.println("Emmiter erro: " + e.getMessage());
//            emitter.complete();
            System.err.println("TA CHEGANDO AQUI 1");
//            emitter.completeWithError(e);
            this.remove(emitter, customerOrder);
            System.err.println("TA CHEGANDO AQUI 2");
        }
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
