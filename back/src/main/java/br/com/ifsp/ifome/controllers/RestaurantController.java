package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.*;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.services.CustomerOrderService;
import br.com.ifsp.ifome.services.RestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@MultipartConfig
@RequestMapping("/api/restaurant/")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final CustomerOrderService customerOrderService;

    public RestaurantController(RestaurantService restaurantService, CustomerOrderService customerOrderService){
        this.restaurantService = restaurantService;
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
        var restaurantResponse = restaurantService.findById(id);

        ApiResponse apiResponse = new ApiResponse("success", restaurantResponse, "Restaurante encontrado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping
    @DocsOpenCloseRestaurant
     public ResponseEntity<ApiResponse> putOpen(Principal principal) {
        String message = restaurantService.changeStateOpen(principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping
    @DocsOpenCloseRestaurant
    public ResponseEntity<ApiResponse> patchOpen(Principal principal) {
        String message = restaurantService.changeStateOpen(principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }

    //TODO arrumar retorno de restaurante sem pedidos
    @GetMapping("/orders")
    @DocsGetAllRestaurantOrders
    public ResponseEntity<ApiResponse> getOrders(Principal principal) {
        List<CustomerOrderResponse> pedidos = restaurantService.getOrders(principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", pedidos, null);
        return ResponseEntity.ok(apiResponse);
    }

    // TODO fazer verificação de customerOrder é do restaurante logado
    @PutMapping("/order/status/{customerOrderId}")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long customerOrderId) {
        customerOrderService.updateOrderStatus(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

    // TODO fazer verificação de customerOrder é do restaurante logado
    @PutMapping("/order/status/{customerOrderId}/previous")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> previousOrderStatus(@PathVariable Long customerOrderId) {
        customerOrderService.previousOrderStatus(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

}
