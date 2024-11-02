package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.services.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/delivery/")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(
        security = @SecurityRequirement(name = "Bearer Token")
    )
    @GetMapping("/orders/")
    public ResponseEntity<ApiResponse> getOrders(Principal principal) {
        List<DeliveryOrderResponse> orders = deliveryService.getOrders(principal);

        return ResponseEntity.ok(new ApiResponse("success", orders, "Pedidos disponíveis"));
    }
}
