package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
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

    public RestaurantResponse findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(RestaurantNotFoundException::new);
        return RestaurantResponse.from(restaurant);
    }

}
