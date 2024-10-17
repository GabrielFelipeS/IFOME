package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsGetAll;
import br.com.ifsp.ifome.docs.DocsGetPagination;
import br.com.ifsp.ifome.docs.DocsGetRestaurantById;
import br.com.ifsp.ifome.docs.DocsOpenCloseRestaurant;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.services.RestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
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
    @DocsGetPagination
    public ResponseEntity<ApiResponse> getRestaurantsPagination(
        @Parameter(hidden = true) @PageableDefault(size = 15, page = 0) Pageable pageable) {
        var restaurantResponses = restaurantService.getAllRestaurants(pageable);

        ApiResponse apiResponse = new ApiResponse("success", restaurantResponses, "Sucesso na busca dos restaurantes");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all")
    @DocsGetAll
    public ResponseEntity<ApiResponse> getAllRestaurant() {
        var restaurantResponses = restaurantService.getAllRestaurants();

        ApiResponse apiResponse = new ApiResponse("success", restaurantResponses, "Sucesso na busca dos restaurantes");
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


    @PostMapping("/{id}/order")
    public ResponseEntity<ApiResponse> createOrder(
            @PathVariable Long id, // ID do restaurante onde o pedido será feito
            @RequestBody @Valid OrderRequest orderRequest, // Dados do pedido
            Principal principal) {

        // Chamar o serviço de criação do pedido
        String message = restaurantService.createOrder(id, orderRequest, principal);

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }

}
