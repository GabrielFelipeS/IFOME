package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsGetAll;
import br.com.ifsp.ifome.docs.DocsGetPagination;
import br.com.ifsp.ifome.docs.DocsGetRestaurantById;
import br.com.ifsp.ifome.docs.DocsOpenCloseRestaurant;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.services.RestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@MultipartConfig
@RequestMapping("/api/restaurant/")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final CustomerOrderRepository customerOrderRepository;

    public RestaurantController(RestaurantService restaurantService, CustomerOrderRepository customerOrderRepository){
        this.restaurantService = restaurantService;
        this.customerOrderRepository = customerOrderRepository;
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

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse> getMapping(Principal principal) {
        List<CustomerOrder> orders = customerOrderRepository.findAllByCartClientEmail(principal.getName());

        var pedidos = orders.stream()
            .map(CustomerOrderResponse::new)
            .collect(Collectors.toList());

        ApiResponse apiResponse = new ApiResponse("success", pedidos, null);
        return ResponseEntity.ok(apiResponse);
    }


}
