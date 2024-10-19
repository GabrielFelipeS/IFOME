package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.entities.Cart;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.repositories.CartRepository;
import br.com.ifsp.ifome.services.CustomerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping("/{id}/order")
    public ResponseEntity<ApiResponse> createOrder(
        Principal principal) {
        String custumerEmail = principal.getName();
        Optional<Cart> cartCustumer =  cartRepository.findFirstByClientEmail(custumerEmail);
        // Chamar o serviço de criação do pedido
        String message = customerOrderService.createOrder(principal);

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }
}
