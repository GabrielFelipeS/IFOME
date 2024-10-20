package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderStatus;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CartRepository cartRepository;

    public CustomerOrderService(RestaurantRepository restaurantRepository, DishRepository dishRepository, CustomerOrderRepository customerOrderRepository, CartRepository cartRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.cartRepository = cartRepository;
    }

    public String createOrder(Principal principal, Cart cart) {
        Cart custumerCart = cartRepository
                        .findFirstByClientEmail(principal.getName())
                        .orElseThrow();

       Restaurant restaurant = restaurantRepository
                                .findById(cart.getIdRestaurant())
                                .orElseThrow(RestaurantNotFoundException::new);

        CustomerOrder customerOrder = new CustomerOrder(cart, restaurant);
        customerOrder.setStatus(OrderStatus.NOVO);  // Define o status inicial como NOVO

        customerOrderRepository.save(customerOrder);

        return "Pedido criado com sucesso!";
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

        // Atualizar o status apenas se for o próximo na sequência
        customerOrder.setStatus(nextStatus);
        customerOrderRepository.save(customerOrder);
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
}
