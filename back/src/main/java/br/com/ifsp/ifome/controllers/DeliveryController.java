package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocUpdateCustomerOrderStatus;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.services.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/info/")
    public ResponseEntity<ApiResponse> getInfo(Principal principal) {
        DeliveryPersonResponse deliveryPersonResponse = deliveryService.getByEmail(principal.getName());

        return ResponseEntity.ok(new ApiResponse("success", deliveryPersonResponse, "Pedidos disponíveis"));
    }


    @Operation(
        security = @SecurityRequirement(name = "Bearer Token")
    )
    @GetMapping("/orders/")
    public ResponseEntity<ApiResponse> getOrders(Principal principal) {
        List<DeliveryOrderResponse> orders = deliveryService.getOrders(principal);

        return ResponseEntity.ok(new ApiResponse("success", orders, "Pedidos disponíveis"));
    }

    @PutMapping("/order/status/{customerOrderId}")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long customerOrderId) {
        deliveryService.updateOrderStatus(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

    @PutMapping("/order/status/{customerOrderId}/previous")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> previousOrderStatus(@PathVariable Long customerOrderId) {
        deliveryService.previousOrderStatus(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

    @PutMapping("/coordinates/")
    public ResponseEntity<ApiResponse> updateCoordinates(
        @RequestBody CoordinatesRequest coordinatesRequest,
        Principal principal) {
        deliveryService.updateCoordinates(coordinatesRequest, principal);

        return ResponseEntity.ok(new ApiResponse("success", null, "Coordenadas atualizadas"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> teste() {

        return ResponseEntity.ok(new ApiResponse("success", null, "Coordenadas atualizadas"));
    }

}
