package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.*;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.request.OrderItemUpdateRequest;
import br.com.ifsp.ifome.dto.request.RestaurantReviewRequest;
import br.com.ifsp.ifome.dto.response.*;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.entities.RestaurantReview;
import br.com.ifsp.ifome.exceptions.client.OrderAlreadyReviewedException;
import br.com.ifsp.ifome.exceptions.client.OrderNotDeliveredException;
import br.com.ifsp.ifome.exceptions.client.OrderNotFoundException;
import br.com.ifsp.ifome.exceptions.client.OrderNotOwnedByClientException;
import br.com.ifsp.ifome.services.*;
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
    private final RestaurantService restaurantService;
    private final RecommendationsService recommendationsService;

    public ClientController(ClientService clientService, CustomerOrderService customerOrderService, SearchService searchService, RestaurantService restaurantService, RecommendationsService recommendationsService) {
        this.clientService = clientService;
        this.customerOrderService = customerOrderService;
        this.searchService = searchService;
        this.restaurantService = restaurantService;
        this.recommendationsService = recommendationsService;
    }

    @PostMapping("/order/")
    @DocsCreateCustomerOrder
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        CustomerOrderResponse customerOrderResponse = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", customerOrderResponse, "Pedido enviado! Aguarde confimação!");
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

    @DocsRestaurantReview
    @PostMapping("/order/{orderId}/review")
    public ResponseEntity<ApiResponse> reviewRestaurant(
            @PathVariable Long orderId,
            @RequestBody @Valid RestaurantReviewRequest reviewRequest,
            Principal principal) {

            RestaurantReview review = restaurantService.reviewRestaurant(orderId, reviewRequest, principal.getName());
            ReviewResponse response = new ReviewResponse(review);

            return ResponseEntity.ok(new ApiResponse("success", response, "Avaliação registrada com sucesso!"));

    }

    @DocsRecommendedRestaurants
    @GetMapping("/recommendations/restaurants")
    public ResponseEntity<ApiResponse>getRecommendedRestaurants(Principal principal){
        String email = principal.getName(); // Obtendo o email do cliente autenticado
        List<RestaurantResponse> recomendations = recommendationsService.recommendsRestaurants(email);
        return ResponseEntity.ok(new ApiResponse("sucess", recomendations, "Restaurantes recomendados com sucesso."));
    }

    @DocsRecommendedDishes
    @GetMapping("recommendations/dishes")
    public ResponseEntity<ApiResponse> getRecommendedDishes(Principal principal){
        String email = principal.getName(); // Obtendo o email do cliente autenticado
        List<DishResponse> recommendations = recommendationsService.recommendDishes(email);
        return ResponseEntity.ok(new ApiResponse("sucess",recommendations, "Pratos Recomendados com sucesso"));
    }




}


