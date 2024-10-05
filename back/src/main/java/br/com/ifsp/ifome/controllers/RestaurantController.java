package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsGetRestaurantById;
import br.com.ifsp.ifome.docs.DocsOpenCloseRestaurant;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.services.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController

@MultipartConfig
@RequestMapping("/api/restaurant/")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @Operation(summary = "Pegar todos os restaurantes de forma paginada", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<ApiResponse> getAllRestaurant(
        @PageableDefault(size = 15, page = 0) Pageable pageable) {
        var restaurantResponses = restaurantService.getAllRestaurants(pageable);

        ApiResponse apiResponse = new ApiResponse("success", restaurantResponses, "Busca com sucesso dos restaurantes");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "Pegar todos os restaurantes", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<ApiResponse> getRestaurantsPagination() {
        var restaurantResponses = restaurantService.getAllRestaurants();

        ApiResponse apiResponse = new ApiResponse("success", restaurantResponses, "Busca com sucesso dos restaurantes");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @DocsGetRestaurantById
    public ResponseEntity<ApiResponse> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.findById(id);
        var restaurantResponse = RestaurantResponse.from(restaurant);
        ApiResponse apiResponse = new ApiResponse("success", restaurantResponse, "Restaurante encontrado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping
    @DocsOpenCloseRestaurant
     public ResponseEntity<ApiResponse> putOpen(Principal principal) {
        String message = restaurantService.changeStateOpen(principal);
        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping
    @DocsOpenCloseRestaurant
    public ResponseEntity<ApiResponse> patchOpen(Principal principal) {
        String message = restaurantService.changeStateOpen(principal);
        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }
}
