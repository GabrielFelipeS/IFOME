package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsGetCart;
import br.com.ifsp.ifome.docs.DocsInsertOrderItemInCart;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.response.CartResponse;
import br.com.ifsp.ifome.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @DocsGetCart
    public ResponseEntity<ApiResponse> getCart(Principal principal) {
        CartResponse cartResponse = clientService.getCart(principal.getName());
        ApiResponse response = new ApiResponse("success", cartResponse, "Carrinho encontrado!");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @DocsInsertOrderItemInCart
    public ResponseEntity<ApiResponse> addDishInCart(@RequestBody @Valid OrderItemRequest orderItemRequest, Principal principal) {
        CartResponse cartResponse = clientService.addDishCart(orderItemRequest, principal.getName());
        System.err.println(cartResponse);
        ApiResponse response = new ApiResponse("success", cartResponse, "Prado adicionado no carrinho");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @Operation(security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<ApiResponse> updateQuantityOrderItemInCart(@RequestBody @Valid OrderItemRequest orderItemRequest, Principal principal) {
        clientService.updateQuantityOrderItemInCart(orderItemRequest, principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, "Quantidade do prato atualizado com sucesso!");

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<ApiResponse> deleteOrderItemInCart(@PathVariable Long id, Principal principal) {
        clientService.removeDishInCart(id, principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, "Quantidade do prato atualizado com sucesso!");

        return ResponseEntity.noContent().build();
    }
}
