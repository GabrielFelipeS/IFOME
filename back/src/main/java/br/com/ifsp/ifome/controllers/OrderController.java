package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{id}/order")
    public ResponseEntity<ApiResponse> createOrder(
        @PathVariable Long id, // ID do restaurante onde o pedido será feito
        @RequestBody @Valid OrderRequest orderRequest, // Dados do pedido
        Principal principal) {
        // Chamar o serviço de criação do pedido
        String message = orderService.createOrder(id, orderRequest, principal);

        ApiResponse apiResponse = new ApiResponse("success", null, message);
        return ResponseEntity.ok(apiResponse);
    }
}
