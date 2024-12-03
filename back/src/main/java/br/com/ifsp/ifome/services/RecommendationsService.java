package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.dto.response.RestaurantReviewResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<RestaurantReviewResponse> recommendsRestaurants(String customerEmail) {
        // Buscar todas as ordens do cliente
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        // Agrupar os pedidos por restaurante e contar a frequência dos restaurantes
        Map<Long, Long> restaurantFrequency = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getRestaurant().getId(), Collectors.counting()));

        // Ordenar os restaurantes pela frequência de pedidos de forma decrescente
        List<Long> recommendedRestaurantIds = restaurantFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)  // Limita a 10 restaurantes
                .collect(Collectors.toList());

        // Obter todos os restaurantes de uma vez
        List<Restaurant> recommendedRestaurants = restaurantRepository.findAllById(recommendedRestaurantIds);

        // Obter a categoria de comida do primeiro restaurante da lista de recomendações
        String foodCategory = recommendedRestaurants.isEmpty() ? "" : recommendedRestaurants.get(0).getFoodCategory();

        // Filtrar os restaurantes pela mesma categoria de comida
        List<Restaurant> filteredRestaurants = recommendedRestaurants.stream()
                .filter(restaurant -> restaurant.getFoodCategory().equals(foodCategory))
                .collect(Collectors.toList());

        // Mapear os restaurantes para objetos de resposta e retornar a lista
        return filteredRestaurants.stream()
                .map(RestaurantReviewResponse::from)
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
            for (OrderItem orderItem : order.getOrderItems()) {
                dishFrequency.put(orderItem.getDishId(),
                        dishFrequency.getOrDefault(orderItem.getDishId(), 0L) + 1);
            }
        }

        // Ordenar os pratos pela frequência de pedidos
        List<Long> recommendedDishIds = dishFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))  // Ordem decrescente
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Lista para armazenar as recomendações
        List<DishResponse> recommendations = new ArrayList<>();

        // Obter todos os pratos de uma vez, em vez de fazer consultas repetidas
        List<Dish> allDishes = dishRepository.findAllAvailable(Sort.by(Sort.Order.asc("dishCategory")));

        // Para cada prato recomendado, pega a categoria e busca outros pratos dessa categoria
        for (Long dishId : recommendedDishIds) {
            // Obtém o prato recomendado
            Dish dish = dishRepository.findById(dishId).orElseThrow();

            // Obtém a categoria do prato
            String category = dish.getDishCategory();

            // Filtra os pratos da mesma categoria
            List<Dish> sameCategoryDishes = allDishes.stream()
                    .filter(d -> d.getDishCategory().equals(category) && !d.getId().equals(dish.getId()))
                    .collect(Collectors.toList());

            // Adiciona os pratos filtrados na lista de recomendações
            for (Dish sameCategoryDish : sameCategoryDishes) {
                if (!recommendations.stream().anyMatch(r -> r.id().equals(sameCategoryDish.getId()))) {
                    recommendations.add(new DishResponse(sameCategoryDish));
                }
            }

            // Limitar o número de recomendações (se necessário)
            if (recommendations.size() >= 10) { // Exemplo de limite de 10 recomendações
                break;
            }
        }

        return recommendations;
    }

}
