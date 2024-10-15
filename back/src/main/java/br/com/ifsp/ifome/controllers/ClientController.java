package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.dto.response.CartResponse;
import br.com.ifsp.ifome.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addDishInCart(@RequestBody OrderRequest orderRequest, Principal principal) {
        CartResponse cartResponse = clientService.addDishCart(orderRequest, principal.getName());

        ApiResponse response = new ApiResponse("success", cartResponse, "Prado adicionado no carrinho");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
