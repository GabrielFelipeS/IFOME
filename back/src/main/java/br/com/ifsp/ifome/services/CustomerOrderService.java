package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.CartCannotBeEmptyException;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.OrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CustomerOrderService {
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public CustomerOrderService(RestaurantRepository restaurantRepository, DishRepository dishRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
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

        orderRepository.save(customerOrder);

        return CustomerOrderRequest.from(customerOrder);
    }
}
