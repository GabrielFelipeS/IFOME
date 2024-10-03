package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public String changeStateOpen(Principal principal) {
        var restaurant = restaurantRepository.findByEmail(principal.getName()).get();
        boolean reverseOpen = !restaurant.isOpen();
        restaurant.setOpen(reverseOpen);
        restaurantRepository.save(restaurant);

        String message = String.format("Restaurante %s com sucesso!", reverseOpen? "aberto" : "fechado");
        return message;
    }
}
