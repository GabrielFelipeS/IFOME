package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderStatus;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.events.PedidoStatusChangedEvent;
import br.com.ifsp.ifome.exceptions.CartCannotBeEmptyException;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.EmitterException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CartRepository cartRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, SseEmitter> sseEmitterHashMap;

    public CustomerOrderService(RestaurantRepository restaurantRepository, DishRepository dishRepository, CustomerOrderRepository customerOrderRepository, CartRepository cartRepository, ApplicationEventPublisher eventPublisher) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.cartRepository = cartRepository;
        this.eventPublisher = eventPublisher;
        this.sseEmitterHashMap = new ConcurrentHashMap<>();
    }

    public CustomerOrderRequest createOrder(Principal principal) {
        Cart cart = cartRepository
            .findFirstByClientEmail(principal.getName())
            .orElseThrow(CartCannotBeEmptyException::new)
            .cartCannotBeEmpty();

        Restaurant restaurant = restaurantRepository
            .findById(cart.getIdRestaurant())
            .orElseThrow(RestaurantNotFoundException::new);

        CustomerOrder customerOrder = new CustomerOrder(cart, restaurant);

        customerOrderRepository.save(customerOrder);

        this.addEmitter(customerOrder);

        return CustomerOrderRequest.from(customerOrder);
    }

    public List<CustomerOrderResponse> getAllOrdersByCustomer(String customerEmail) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        return orders.stream()
                .map(CustomerOrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<CustomerOrderResponse> getAllOrdersByRestaurant(String restaurantEmail) {
        // Obtenha o restaurante correspondente ao email
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByEmail(restaurantEmail);

        // Verifique se o restaurante foi encontrado
        Restaurant restaurant = restaurantOpt.orElseThrow(() ->
                new EntityNotFoundException("Restaurante não encontrado com o email: " + restaurantEmail)
        );

        // Busque todos os pedidos associados a este restaurante
        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantId(restaurant.getId());

        // Converta as entidades CustomerOrder em CustomerOrderResponse
        return orders.stream()
                .map(CustomerOrderResponse::new) // Assumindo que você tem um construtor adequado
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(Long orderId) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        OrderStatus currentStatus = customerOrder.getStatus();
        OrderStatus nextStatus = getNextStatus(currentStatus);

        // Atualize o status do pedido
        customerOrder.setStatus(nextStatus);
        customerOrderRepository.save(customerOrder);

        this.updateStatusEmitter(customerOrder);
        eventPublisher.publishEvent(new PedidoStatusChangedEvent(orderId, nextStatus, customerOrder));
    }

    private OrderStatus getNextStatus(OrderStatus currentStatus) {
        List<OrderStatus> validSequence = List.of(
            OrderStatus.NOVO,
            OrderStatus.ACEITO,
            OrderStatus.EM_PREPARO,
            OrderStatus.PRONTO_PARA_ENTREGA,
            OrderStatus.SAIU_PARA_ENTREGA,
            OrderStatus.CONCLUIDO
        );

        int currentIndex = validSequence.indexOf(currentStatus);
        if (currentIndex == -1 || currentIndex == validSequence.size() - 1) {
            throw new IllegalStateException("O status atual não pode ser alterado.");
        }

        // Retorna o próximo status na sequência
        return validSequence.get(currentIndex + 1);
    }

    public SseEmitter getEmitter(Long id) {
        System.err.println(id);
        Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(id);
        SseEmitter emitter =  this.getEmitter(customerOrder.get());
        System.err.println(emitter);
        return emitter;
    }

    public SseEmitter getEmitter(CustomerOrder customerOrder) {
        if(sseEmitterHashMap.containsKey(customerOrder.getId())) {
            return sseEmitterHashMap.get(customerOrder.getId());
        }

        return this.addEmitter(customerOrder);
    }


    private SseEmitter addEmitter(CustomerOrder customerOrder) {
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

    private void updateStatusEmitter(CustomerOrder customerOrder)  {
        System.err.println("PEGANDO EMITTER");
        SseEmitter emitter = this.getEmitter(customerOrder);
        System.err.println(emitter);
        try {
            System.err.println("ANTES DE ENVIAR MENSAGEM EMITTER");
            emitter.send(customerOrder.getStatusMessage());
            System.err.println("DEPOIS DE ENVIAR MENSAGEM EMITTER");
        } catch (IOException e) {
            throw new EmitterException("Emmiter erro: " + e.getMessage());
        }
    }

}
