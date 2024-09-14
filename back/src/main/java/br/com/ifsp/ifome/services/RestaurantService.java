package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponse create(RestaurantRequest restaurantRequest){
        Restaurant restaurant = new Restaurant(restaurantRequest);
        restaurant = restaurantRepository.save(restaurant);
        return new RestaurantResponse(restaurant);
    }

}
