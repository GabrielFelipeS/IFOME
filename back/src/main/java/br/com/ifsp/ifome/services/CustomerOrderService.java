package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
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

    public String createOrder(Principal principal) {
        Cart cart = cartRepository
                        .findFirstByClientEmail(principal.getName())
                        .orElseThrow();

       Restaurant restaurant = restaurantRepository
                                .findById(cart.getIdRestaurant())
                                .orElseThrow(RestaurantNotFoundException::new);

        CustomerOrder customerOrder = new CustomerOrder(cart, restaurant);

        customerOrderRepository.save(customerOrder);

        return "Pedido criado com sucesso!";
    }

    public List<CustomerOrderResponse> getAllOrdersByCustomer(String customerEmail) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        return orders.stream()
                .map(CustomerOrderResponse::new)
                .collect(Collectors.toList());
    }
}
