package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderStatus;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.CartCannotBeEmptyException;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {
    private final RestaurantRepository restaurantRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CartRepository cartRepository;
    private final EventStatusEmitterService eventStatusEmitterService;

    public CustomerOrderService(RestaurantRepository restaurantRepository, CustomerOrderRepository customerOrderRepository,
                                CartRepository cartRepository,
                                EventStatusEmitterService eventStatusEmitterService) {
        this.restaurantRepository = restaurantRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.cartRepository = cartRepository;
        this.eventStatusEmitterService = eventStatusEmitterService;
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

        eventStatusEmitterService.addEmitter(customerOrder);

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

       OrderStatus orderStatus = customerOrder.nextStatus();

        customerOrderRepository.save(customerOrder);

        eventStatusEmitterService.updateStatusEmitter(customerOrder, orderStatus);
    }

    private OrderStatus getNextStatus(OrderStatus currentStatus) {
        List<OrderStatus> validSequence = List.of(
            OrderStatus.NOVO,
            OrderStatus.EM_PREPARO,
            OrderStatus.PRONTO_PARA_ENTREGA,
            OrderStatus.SAIU_PARA_ENTREGA,
            OrderStatus.CONCLUIDO
        );

        int currentIndex = validSequence.indexOf(currentStatus);
        if (currentIndex == -1 || currentIndex == validSequence.size() - 1) {
            throw new IllegalStateException("O status atual não pode ser alterado.");
        }
        System.err.println("currentIndex: " + currentIndex);

        return validSequence.get(currentIndex + 1);
    }

    public SseEmitter getEmitter(Long id) {
        System.err.println(id);
        Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(id);
        SseEmitter emitter =  eventStatusEmitterService.getEmitter(customerOrder.get());
        System.err.println(emitter);
        return emitter;
    }

}
