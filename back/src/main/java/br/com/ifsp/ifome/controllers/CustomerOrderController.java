package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.UpdateOrderStatusRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.dto.response.OrderItemResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.services.CustomerOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;
    private final CartRepository cartRepository;

    public CustomerOrderController(CustomerOrderService customerOrderService, CartRepository cartRepository) {
        this.customerOrderService = customerOrderService;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createOrder(Principal principal) {
        String customerEmail = principal.getName();
        Optional<Cart> cartCustomer = cartRepository.findFirstByClientEmail(customerEmail);

        // Verifica se o carrinho do cliente existe
        if (cartCustomer.isPresent()) {
            // Chama o serviço de criação do pedido, passando o carrinho e o principal
            String message = customerOrderService.createOrder(principal, cartCustomer.get());
            ApiResponse apiResponse = new ApiResponse("success", null, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } else {
            // Retorna um erro se o carrinho não for encontrado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Carrinho não encontrado", null));
        }
    }

    @GetMapping("/customerOrders")
    public ResponseEntity<List<CustomerOrderResponse>> getAllCustomerOrders(Principal principal) {
        // Check if the principal is present
        //if (principal == null || principal.getName() == null) {
          //  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if user is not authenticated
        //}

        String customerEmail = principal.getName();
        List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByCustomer(customerEmail);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no orders found
        }

        return ResponseEntity.ok(orders); // Return 200 OK with the list of orders
    }

    @GetMapping("/restaurantOrders")
    public ResponseEntity<List<CustomerOrderResponse>> getAllRestaurantOrders(Principal principal) {
        String  restaurantEmail = principal.getName(); // Ou use outro método para identificar o restaurante

        List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByRestaurant(restaurantEmail);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retornar 204 No Content se não houver pedidos
        }

        return ResponseEntity.ok(orders); // Retornar 200 OK com a lista de pedidos
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<ApiResponse> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        try {
            customerOrderService.updateOrderStatus(request.customerOrderId(), request.newStatus());
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
}
