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

import java.util.*;
import java.util.stream.Collectors;
/**
 * Serviço responsável por fornecer recomendações de restaurantes e pratos com base no histórico de pedidos do cliente.
 */
@Service
public class RecommendationsService {

    private final CustomerOrderRepository customerOrderRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    /**
     * Construtor para injeção das dependências.
     *
     * @param customerOrderRepository Repositório para acessar dados de pedidos de clientes.
     * @param dishRepository Repositório para acessar dados de pratos.
     * @param restaurantRepository Repositório para acessar dados de restaurantes.
     */
    public RecommendationsService(CustomerOrderRepository customerOrderRepository,
                                 DishRepository dishRepository,
                                 RestaurantRepository restaurantRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Retorna uma lista de restaurantes recomendados com base no histórico de pedidos do usuário.
     * A recomendação leva em consideração a frequência de pedidos realizados pelo cliente em cada restaurante.
     * Os restaurantes mais requisitados serão priorizados, e a recomendação será feita considerando a categoria de comida
     * mais frequente entre esses restaurantes.
     *
     * @param customerEmail O e-mail do cliente para buscar o histórico de pedidos e calcular as recomendações.
     * @return Uma lista de objetos {@link RestaurantReviewResponse} contendo os restaurantes recomendados,
     *         ou uma lista vazia caso nenhum restaurante tenha sido recomendado.
     */
    public List<RestaurantReviewResponse> recommendsRestaurants(String customerEmail) {
        // Buscar todas as ordens do cliente
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(customerEmail);

        // Agrupar os pedidos por restaurante e contar a frequência dos restaurantes
        Map<Long, Long> restaurantFrequency = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getRestaurant().getId(), Collectors.counting()));

        // Ordenar os restaurantes pela frequência de pedidos de forma decrescente e limitar a 10
        List<Long> recommendedRestaurantIds = restaurantFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)  // Limita a 10 restaurantes
                .collect(Collectors.toList());

        // Buscar os restaurantes diretamente pelos IDs recomendados, incluindo a categoria de comida
        List<Restaurant> recommendedRestaurants = restaurantRepository.findAllById(recommendedRestaurantIds);

        // Se não houver restaurantes recomendados, retorne uma lista vazia
        if (recommendedRestaurants.isEmpty()) {
            return Collections.emptyList();
        }

        // Obter a categoria de comida do primeiro restaurante da lista
        String foodCategory = recommendedRestaurants.get(0).getFoodCategory();

        // Filtrar os restaurantes pela mesma categoria de comida, se houver
        return recommendedRestaurants.stream()
                .filter(restaurant -> restaurant.getFoodCategory().equals(foodCategory))
                .map(RestaurantReviewResponse::from)
                .collect(Collectors.toList());
    }


    /**
     * Retorna uma lista de pratos recomendados com base no histórico de pedidos do usuário.
     * A recomendação leva em consideração a frequência de pedidos por prato, priorizando os mais requisitados.
     * Para cada prato recomendado, outros pratos da mesma categoria também são sugeridos.
     *
     * @param customerEmail O e-mail do cliente para buscar o histórico de pedidos.
     * @return Uma lista de pratos recomendados.
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
