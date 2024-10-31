package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.*;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.services.CustomerOrderService;
import br.com.ifsp.ifome.services.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@MultipartConfig
@RequestMapping("/api/restaurant/")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final CustomerOrderRepository customerOrderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerOrderService customerOrderService;

    public RestaurantController(RestaurantService restaurantService, CustomerOrderRepository customerOrderRepository, RestaurantRepository restaurantRepository, CustomerOrderService customerOrderService){
        this.restaurantService = restaurantService;
        this.customerOrderRepository = customerOrderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerOrderService = customerOrderService;
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

//    /@Operation(
//        security = @SecurityRequirement(name = "Bearer Token")
//    )

    //TODO arrumar retorno de restaurante sem pedidos
    @GetMapping("/orders")
    @DocsGetAllRestaurantOrders
    public ResponseEntity<ApiResponse> getMapping(Principal principal) {
        // TODO refactor method
        System.err.println(principal.getName());
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByEmail(principal.getName());

        // Verifique se o restaurante foi encontrado
        Restaurant restaurant = restaurantOpt.orElseThrow(() ->
            new EntityNotFoundException("Restaurante não encontrado com o email: " + principal.getName())
        );

        // Busque todos os pedidos associados a este restaurante
        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantId(restaurant.getId());
        orders.stream().forEach(System.err::println);
        var pedidos = orders.stream()
            .map(CustomerOrderResponse::new)
            .collect(Collectors.toList());

        ApiResponse apiResponse = new ApiResponse("success", pedidos, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/order/status/{customerOrderId}")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long customerOrderId) {
        try {
            customerOrderService.updateOrderStatus(customerOrderId);
            return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", null, e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", null, e.getMessage()));
        } catch (Exception e) {
            // Catch any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", null, "Erro inesperado: " + e.getMessage()));
        }
    }

    @PutMapping("/order/status/{customerOrderId}/previous")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> previousOrderStatus(@PathVariable Long customerOrderId) {
            customerOrderService.previousOrderStatus(customerOrderId);
            return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

}
