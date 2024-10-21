package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsCreateCustomerOrder;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.UpdateOrderStatusRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.services.CustomerOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderController {

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

    SseEmitter emitter;

    @GetMapping("/status")
    public SseEmitter getStatusCustomerOrder() throws IOException {
        emitter = new SseEmitter(Long.MAX_VALUE);

        Long clienteId = 1L;
        System.err.println("AQUI");
        emitter.send("Status atual: Pedido recebido");

        emitter.onTimeout(() -> emitter.onTimeout(() -> System.out.println("Parou")));
        emitter.onError((e) -> emitter.onError(System.err::println));
        emitter.onCompletion(() -> emitter.complete());

        return emitter;
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

    @Operation(
        summary = "FInalizar pedido com pratos do carrinho",
        security = @SecurityRequirement(name = "Bearer Token")
    )
    @PutMapping("/updateStatus")
    public ResponseEntity<ApiResponse> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        try {
            customerOrderService.updateOrderStatus(request.customerOrderId());
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

    @GetMapping("/teste")
    public void teste() throws IOException {
        emitter.send("Teste " + LocalDateTime.now().getSecond());
    }


}
