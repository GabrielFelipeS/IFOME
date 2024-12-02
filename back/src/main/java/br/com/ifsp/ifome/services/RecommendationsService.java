package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationsService {

    private final CustomerOrderRepository customerOrderRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public RecommendationsService(CustomerOrderRepository customerOrderRepository,
                                 DishRepository dishRepository,
                                 RestaurantRepository restaurantRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Retorna uma lista de restaurantes recomendados para o usuário
     */
    public List<RestaurantResponse> recommendsRestaurants(String customerEmail) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        // Agrupar por restaurante e calcular a frequência
        Map<Long, Long> restaurantFrequency = new HashMap<>();
        for (CustomerOrder order : orders) {
            restaurantFrequency.put(order.getRestaurant().getId(),
                    restaurantFrequency.getOrDefault(order.getRestaurant().getId(), 0L) + 1);
        }

        // Ordenar os restaurantes pela frequência de pedidos
        List<Long> recommendedRestaurantIds = restaurantFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))  // Ordem decrescente
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedRestaurantIds.stream()
                .map(restaurantId -> RestaurantResponse.from(restaurantRepository.findById(restaurantId).orElseThrow()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna uma lista de pratos recomendados para o usuário
     */
    public List<DishResponse> recommendDishes(String customerEmail) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        // Agrupar por prato e calcular a frequência
        Map<Long, Long> dishFrequency = new HashMap<>();
        for (CustomerOrder order : orders) {
            for (OrderItem dish : order.getOrderItems()) {
                dishFrequency.put(dish.getDishId(),
                        dishFrequency.getOrDefault(dish.getDishId(), 0L) + 1);
            }
        }

        // Ordenar os pratos pela frequência de pedidos
        List<Long> recommendedDishIds = dishFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))  // Ordem decrescente
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedDishIds.stream()
                .map(dishId -> new DishResponse(dishRepository.findById(dishId).orElseThrow()))
                .collect(Collectors.toList());
    }
}
