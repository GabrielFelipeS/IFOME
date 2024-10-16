package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsOpenCloseRestaurant;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.dto.response.CartResponse;
import br.com.ifsp.ifome.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCart(Principal principal) {
        CartResponse cartResponse = clientService.getCart(principal.getName());
        ApiResponse response = new ApiResponse("success", cartResponse, "Prado adicionado no carrinho");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addDishInCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        CartResponse cartResponse = clientService.addDishCart(orderRequest, principal.getName());

        ApiResponse response = new ApiResponse("success", cartResponse, "Prado adicionado no carrinho");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateQuantityOrderItemInCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        clientService.updateQuantityOrderItemInCart(orderRequest, principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, "Quantidade do prato atualizado com sucesso!");

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> updateQuantityOrderItemInCart(@PathVariable Long id, Principal principal) {
        clientService.removeDishInCart(id, principal.getName());

        ApiResponse apiResponse = new ApiResponse("success", null, "Quantidade do prato atualizado com sucesso!");

        return ResponseEntity.noContent().build();
    }
}
