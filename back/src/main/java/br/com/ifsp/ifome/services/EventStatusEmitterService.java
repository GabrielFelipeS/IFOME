package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.events.PedidoStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.EmitterException;

import java.io.IOException;
import java.util.Map;

@Service
public class EventStatusEmitterService {
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, SseEmitter> sseEmitterHashMap;

    public EventStatusEmitterService(ApplicationEventPublisher eventPublisher, Map<Long, SseEmitter> sseEmitterHashMap) {
        this.eventPublisher = eventPublisher;
        this.sseEmitterHashMap = sseEmitterHashMap;
    }

    public SseEmitter getEmitter(CustomerOrder customerOrder) {
        if(sseEmitterHashMap.containsKey(customerOrder.getId())) {
            return sseEmitterHashMap.get(customerOrder.getId());
        }

        return this.addEmitter(customerOrder);
    }


    public SseEmitter addEmitter(CustomerOrder customerOrder) {
        System.err.println("CRIANDO EMITTER");
        SseEmitter emitter = new SseEmitter(3_600_000L );

        System.err.println("ONTIMEOUT EMITTER");
        emitter.onTimeout(() -> sseEmitterHashMap.remove(customerOrder.getId()));

        System.err.println("ONCOMPLETION EMITTER");
        emitter.onCompletion(() -> {
            sseEmitterHashMap.remove(customerOrder.getId());
            System.err.println("EXECUTOU E NÂO DEVIA");
            emitter.complete();
        });

        System.err.println("ONERROR EMITTER");
        emitter.onError((e) -> System.err.println("Emmiter Erro: " + e.getMessage()));

        System.err.println("ADICIONANDO EMITTER");
        sseEmitterHashMap.put(customerOrder.getId(), emitter);
        System.err.println(customerOrder.getId());
        return emitter;
    }

    public void updateStatusEmitter(CustomerOrder customerOrder)  {
        System.err.println("PEGANDO EMITTER");
        SseEmitter emitter = this.getEmitter(customerOrder);
        System.err.println(emitter);
        try {
            System.err.println("ANTES DE ENVIAR MENSAGEM EMAIL");
            eventPublisher.publishEvent(new PedidoStatusChangedEvent(customerOrder.getId(), customerOrder.getStatus(), customerOrder));
            System.err.println("DEPOIS DE ENVIAR MENSAGEM EMAIL");

            System.err.println("ANTES DE ENVIAR MENSAGEM EMITTER");
            emitter.send(customerOrder.getStatusMessage());
            System.err.println("DEPOIS DE ENVIAR MENSAGEM EMITTER");

        } catch (IOException e) {
            throw new EmitterException("Emmiter erro: " + e.getMessage());
        }
    }

}
