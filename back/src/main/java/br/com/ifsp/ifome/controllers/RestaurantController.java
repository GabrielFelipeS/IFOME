package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsCreateRestaurant;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.services.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController

@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }
    @DocsCreateRestaurant
    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantRequest restaurantRequest, UriComponentsBuilder ucb){
        RestaurantResponse restaurantResponse = restaurantService.create(restaurantRequest);

        URI locationOfNewRestaurant = ucb
                .path("restaurant/{id}")
                .buildAndExpand(1)
                .toUri();
        return ResponseEntity.created(locationOfNewRestaurant).body(restaurantResponse);
    }



}
