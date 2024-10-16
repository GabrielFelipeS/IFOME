package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Order;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.ResourceNotFoundException;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.OrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, DishRepository dishRepository, OrderRepository orderRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
    }

    public String changeStateOpen(Principal principal) {
        var restaurant = restaurantRepository.findByEmail(principal.getName()).get();
        restaurant.reverseStatusOpen();
        restaurantRepository.save(restaurant);

        String message = String.format("Restaurante %s com sucesso!", restaurant.isOpen()? "aberto" : "fechado");
        return message;
    }

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "nameRestaurant")).stream().map(RestaurantResponse::from).collect(Collectors.toList());
    }

     public Page<RestaurantResponse> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.ASC,"nameRestaurant"))
        )).map(RestaurantResponse::from);
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
            .orElseThrow(RestaurantNotFoundException::new);
    }

    // Novo metodo para criar pedido
    public String createOrder(Long restaurantId, OrderRequest orderRequest, Principal principal) {
        // Validar se o restaurante existe
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        // Criar e salvar os itens do pedido
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : orderRequest.items()) {
            Dish dish = dishRepository.findById(itemRequest.dishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prato não encontrado"));
            orderItems.add(new OrderItem(dish, itemRequest.quantity(), null)); // Assumindo OrderItem sem Cart aqui
        }

        // Criar o pedido
        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setOrderItems(orderItems);
        order.calculateTotalPrice(); // Calcular o preço total do pedido

        // Salvar o pedido
        orderRepository.save(order);

        return "Pedido criado com sucesso!";
    }
}
