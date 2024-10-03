package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.exceptions.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@MultipartConfig
@RequestMapping("/api/restaurant/")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurant() {
        return ResponseEntity.ok(restaurantRepository.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        var restaurant = restaurantRepository.findById(id).orElseThrow(RestaurantNotFoundException::new);
        var restaurantResponse = RestaurantResponse.from(restaurant);
        return ResponseEntity.ok(restaurantResponse);
    }

}
