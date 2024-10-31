package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.*;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.UpdateOrderStatusRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.services.CustomerOrderService;
import com.zaxxer.hikari.HikariDataSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderController {

    @Autowired
    private HikariDataSource dataSource;

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping
    @DocsCreateCustomerOrder
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        CustomerOrderRequest customerOrderRequest = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", customerOrderRequest, "Pedido enviado! Aguarde confimação!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(
        security = @SecurityRequirement(name = "Bearer Token")
    )
    @GetMapping("/customerOrders")
    @DocGetCustomerOrders
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

    @Operation(
        security = @SecurityRequirement(name = "Bearer Token")
    )

    @DocGetRestaurantOrders
    @GetMapping("/restaurantOrders")
    public ResponseEntity<List<CustomerOrderResponse>> getAllRestaurantOrders(Principal principal) {
        String  restaurantEmail = principal.getName(); // Ou use outro método para identificar o restaurante

        List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByRestaurant(restaurantEmail);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retornar 204 No Content se não houver pedidos
        }

        return ResponseEntity.ok(orders); // Retornar 200 OK com a lista de pedidos
    }

//    @Operation(
//        security = @SecurityRequirement(name = "Bearer Token")
//    )

    @PutMapping("/updateStatus/{customerOrderId}")
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

    @PutMapping("/teste")
    public ResponseEntity<ApiResponse> teste(@RequestBody UpdateOrderStatusRequest request) {

        new Thread(() -> {
            try {
                System.err.println("Teste");
                int totalConnections = dataSource.getHikariPoolMXBean().getTotalConnections();
                int activeConnections = dataSource.getHikariPoolMXBean().getActiveConnections();
                int idleConnections = dataSource.getHikariPoolMXBean().getIdleConnections();

                System.out.println("Total Connections: " + totalConnections);
                System.out.println("Active Connections: " + activeConnections);
                System.out.println("Idle Connections: " + idleConnections);
                List<CustomerOrderResponse> orders = customerOrderService.getAllOrdersByCustomer("email1@email.com");
                Thread.sleep(30000);
                System.err.println("Saiu um");
            } catch (Exception e) {
            }
        }).start();

      return ResponseEntity.ok(null);
    }
}
