package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.services.CustomerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @PostMapping("/{id}/order")
    public ResponseEntity<ApiResponse> createOrder(
        @PathVariable Long id, // ID do restaurante onde o pedido será feito
        @RequestBody @Valid OrderRequest orderRequest, // Dados do pedido
        Principal principal) {
        // Chamar o serviço de criação do pedido
        String message = customerOrderService.createOrder(id, orderRequest, principal);

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }
}
