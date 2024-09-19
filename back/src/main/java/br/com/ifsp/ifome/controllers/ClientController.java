package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.docs.DocsCreateClient;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @DocsCreateClient
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ClientRequest clientRequest , UriComponentsBuilder ucb) throws MethodArgumentNotValidException {
        ClientResponse clientResponse = clientService.create(clientRequest);

        URI locationOfNewClient = ucb
            .path("client/{id}")
            .buildAndExpand(clientResponse.id())
            .toUri();

        ApiResponse response = new ApiResponse("success", clientResponse, "Cliente criado com sucesso");
        return ResponseEntity.created(locationOfNewClient).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest clientLogin) {
        LoginResponse loginResponse = clientService.login(clientLogin);
        ApiResponse apiResponse = new ApiResponse("sucess", loginResponse, "Cliente logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }
}
