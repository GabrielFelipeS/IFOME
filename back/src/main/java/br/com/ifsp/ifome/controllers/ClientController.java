package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.*;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.request.OrderItemUpdateRequest;
import br.com.ifsp.ifome.dto.response.*;
import br.com.ifsp.ifome.services.ClientService;
import br.com.ifsp.ifome.services.CustomerOrderService;
import br.com.ifsp.ifome.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;
    private final CustomerOrderService customerOrderService;
    private final SearchService searchService;

    public ClientController(ClientService clientService, CustomerOrderService customerOrderService, SearchService searchService) {
        this.clientService = clientService;
        this.customerOrderService = customerOrderService;
        this.searchService = searchService;
    }

    @PostMapping("/order/")
    @DocsCreateCustomerOrder
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        CustomerOrderRequest customerOrderRequest = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", customerOrderRequest, "Pedido enviado! Aguarde confimação!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/order/")
    @DocGetCustomerOrders
    public ResponseEntity<List<CustomerOrderResponse>> getAllCustomerOrders(Principal principal) {
        List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByCustomer(principal.getName());

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/cart")
    @DocsGetCart
    public ResponseEntity<ApiResponse> getCart(Principal principal) {
        CartResponse cartResponse = clientService.getCart(principal.getName());
        ApiResponse response = new ApiResponse("success", cartResponse, "Carrinho encontrado!");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/cart/dish/")
    @DocsInsertOrderItemInCart
    public ResponseEntity<ApiResponse> addDishInCart(@RequestBody @Valid OrderItemRequest orderItemRequest, Principal principal) {
        CartResponse cartResponse = clientService.addDishCart(orderItemRequest, principal.getName());
        System.err.println(cartResponse);
        ApiResponse response = new ApiResponse("success", cartResponse, "Prato adicionado no carrinho");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/cart/dish/")
    @DocsUpdateItemInCart
    public ResponseEntity<ApiResponse> updateQuantityOrderItemInCart(@RequestBody @Valid OrderItemUpdateRequest orderItemRequest, Principal principal) {
        clientService.updateQuantityOrderItemInCart(orderItemRequest, principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, "Quantidade do prato atualizado com sucesso!");

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/cart/dish/{id}")
    @DocsDeleteDishInCart
    public ResponseEntity<ApiResponse> deleteOrderItemInCart(@PathVariable Long id, Principal principal) {
        clientService.removeDishInCart(id, principal.getName());

        return ResponseEntity.noContent().build();
    }

    @DocsClearCart
    @DeleteMapping("/cart/clear/")
    public ResponseEntity<ApiResponse> clearCart(Principal principal) {
        clientService.clearCart(principal.getName());

        return ResponseEntity.noContent().build();
    }
    @DocsSearch
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(@RequestParam("query") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", null, "O termo de pesquisa é obrigatório."));
        }

        List<RestaurantResponse> restaurants = searchService.searchRestaurants(query);
        List<DishResponse> dishes = searchService.searchDishes(query);

        if (restaurants.isEmpty() && dishes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 caso não haja resultados
        }

        Map<String, Object> result = Map.of(
                "restaurants", restaurants,
                "dishes", dishes
        );

        ApiResponse response = new ApiResponse("success", result, "Resultados encontrados.");
        return ResponseEntity.ok(response);
    }

}
