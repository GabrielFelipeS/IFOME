package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public SearchService(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    public List<RestaurantResponse> searchRestaurants(String query) {
        System.out.println("Buscando restaurantes com o termo: " + query); // Log para verificar o parâmetro
        List<Restaurant> restaurants = restaurantRepository.findByNameRestaurantContainingIgnoreCase(query);
        System.out.println("Restaurantes encontrados: " + restaurants.size()); // Log para verificar a quantidade de resultados
        return restaurants.stream()
                .map(RestaurantResponse::from) // Use the from() method here
                .toList();
    }

    public List<DishResponse> searchDishes(String query) {
        System.out.println("Buscando pratos com o termo: " + query); // Log para verificar o parâmetro
        List<Dish> dishes = dishRepository.findByNameContainingIgnoreCase(query);
        System.out.println("Pratos encontrados: " + dishes.size()); // Log para verificar a quantidade de resultados
        return dishes.stream()
                .map(DishResponse::new)
                .toList();
    }
}
