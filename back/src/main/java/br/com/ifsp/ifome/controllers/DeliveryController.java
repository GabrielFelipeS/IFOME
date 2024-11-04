package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocUpdateCustomerOrderStatus;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.dto.request.RefuseOrderRequest;
import br.com.ifsp.ifome.dto.response.DeliveryOrderResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.services.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
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

//    @Operation(
//        security = @SecurityRequirement(name = "Bearer Token")
//    )
//    @GetMapping("/orders/")
//    public ResponseEntity<ApiResponse> getOrders(Principal principal) {
//        List<DeliveryOrderResponse> orders = deliveryService.getOrders(principal);
//
//        return ResponseEntity.ok(new ApiResponse("success", orders, "Pedidos disponíveis"));
//    }

    // TODO fazer verificação de customerOrder é do entregador logado
    @PutMapping("/order/status/{customerOrderId}")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long customerOrderId) {
        deliveryService.updateOrderStatus(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

    // TODO fazer verificação de customerOrder é do entregador logado
    @PutMapping("/order/status/{customerOrderId}/refuse")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> refuseOrder(@PathVariable Long customerOrderId, RefuseOrderRequest refuseOrderRequest, Principal principal) {
        deliveryService.refuseOrder(customerOrderId, refuseOrderRequest.justification(), principal);
        return ResponseEntity.ok(new ApiResponse("success", null, "Pedido recusado com sucesso!"));
    }

    // TODO fazer verificação de customerOrder é do entregador logado
    @PutMapping("/order/status/{customerOrderId}/previous")
    @DocUpdateCustomerOrderStatus
    public ResponseEntity<ApiResponse> previousOrderStatus(@PathVariable Long customerOrderId) {
        deliveryService.previousOrderStatus(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Status atualizado com sucesso!"));
    }

    @Operation(
        security = @SecurityRequirement(name = "Bearer Token")
    )
    @GetMapping("/order/")
    public ResponseEntity<ApiResponse> getOrder(Principal principal) {
        var responseOptional = deliveryService.getCustomerOrderId(principal);
        System.err.println("AQUI 2");
        if(responseOptional.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse("success", Collections.emptyList(), "Buscando pedido!"));
        }

        return ResponseEntity.ok(new ApiResponse("success", responseOptional.get(), "Pedido atual pego com sucesso!"));
    }

    @PutMapping("/coordinates/")
    public ResponseEntity<ApiResponse> updateCoordinates(
        @RequestBody CoordinatesRequest coordinatesRequest,
        Principal principal) {
        deliveryService.updateCoordinates(coordinatesRequest, principal);

        return ResponseEntity.ok(new ApiResponse("success", null, "Coordenadas atualizadas"));
    }

    @PutMapping("/choice/delivery/{customerOrderId}")
    public ResponseEntity<ApiResponse> teste(@PathVariable Long customerOrderId) {
        deliveryService.simuleChoice(customerOrderId);
        return ResponseEntity.ok(new ApiResponse("success", null, "Coordenadas atualizadas"));
    }

    @Operation(
        security = @SecurityRequirement(name = "Bearer Token")
    )
    @PutMapping("/info/available/")
    public ResponseEntity<ApiResponse> setAvailable(Principal principal) {
        String message = deliveryService.setAvailable(principal);
        return ResponseEntity.ok(new ApiResponse("success", null, message));
    }
}
