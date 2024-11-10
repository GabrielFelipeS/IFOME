package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderClientStatus;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.client.CartCannotBeEmptyException;
import br.com.ifsp.ifome.exceptions.restaurant.OrderNotFromRestaurantException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final CartRepository cartRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final DeliveryService deliveryService;
    private final RestaurantService restaurantService;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository,
                                CartRepository cartRepository,
                                OrderStatusUpdateService orderStatusUpdateService, DeliveryService deliveryService, RestaurantService restaurantService) {
        this.customerOrderRepository = customerOrderRepository;
        this.cartRepository = cartRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.deliveryService = deliveryService;
        this.restaurantService = restaurantService;
    }

    public CustomerOrderRequest createOrder(Principal principal) {
        Cart cart = cartRepository
            .findFirstByClientEmail(principal.getName())
            .orElseThrow(CartCannotBeEmptyException::new)
            .cartCannotBeEmpty();

        Restaurant restaurant = restaurantService.findById(cart.getIdRestaurant());

        CustomerOrder customerOrder = new CustomerOrder(cart, restaurant);

        customerOrderRepository.save(customerOrder);

        return CustomerOrderRequest.from(customerOrder);
    }

    public List<CustomerOrderResponse> getAllOrdersByCustomer(String customerEmail) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        return orders.stream()
                .map(CustomerOrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<CustomerOrderResponse> getAllOrdersByRestaurant(String restaurantEmail) {
        Restaurant restaurant = restaurantService.findByEmail(restaurantEmail);

        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantId(restaurant.getId());

        if(orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(CustomerOrderResponse::new) // Assumindo que você tem um construtor adequado
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(Long orderId, String email) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        if(customerOrder.getRestaurantEmailDoesNotEquals(email)) {
            throw new OrderNotFromRestaurantException("O pedido não pertence ao restaurante logado");
        }

        OrderClientStatus orderClientStatus = customerOrder.nextStatus();

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToClient(customerOrder, orderClientStatus);

        deliveryService.choiceDeliveryPersonWhenReady(customerOrder);
    }

    public void previousOrderStatus(Long orderId, String email) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + orderId));

        if(customerOrder.getRestaurantEmailDoesNotEquals(email)) {
            throw new OrderNotFromRestaurantException("O pedido não pertence ao restaurante logado");
        }

        OrderClientStatus orderClientStatus = customerOrder.previousStatus();

        customerOrderRepository.save(customerOrder);

        orderStatusUpdateService.updateStatusOrderToClient(customerOrder, orderClientStatus);
    }
}
