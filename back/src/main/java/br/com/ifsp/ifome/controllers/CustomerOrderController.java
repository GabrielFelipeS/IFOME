package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.dto.response.OrderItemResponse;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.services.CustomerOrderService;
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
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        String custumerEmail = principal.getName();
        Optional<Cart> cartCustumer =  cartRepository.findFirstByClientEmail(custumerEmail);
        // Chamar o serviço de criação do pedido
        String message = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
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
}
