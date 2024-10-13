package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.aspect.Login;
import br.com.ifsp.ifome.aspect.SensiveData;
import br.com.ifsp.ifome.docs.DocsClientLogin;
import br.com.ifsp.ifome.docs.DocsCreateClient;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.ForgotPasswordRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.services.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth/client")
public class AuthClientController {
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public AuthClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @SensiveData
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

    @SensiveData @Login
    @PostMapping("/login")
    @DocsClientLogin
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = clientService.login(loginRequest);
        ApiResponse apiResponse = new ApiResponse("success", loginResponse, "Cliente logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/forgot_password")
    public void forgotPassword(HttpServletRequest request, @RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        System.err.println(request.getServerName());
        System.out.println(forgotPasswordRequest.email());
        clientService.forgotPassword(request, forgotPasswordRequest.email());
    }

    @GetMapping("/change_password")
    public void changePassword(@RequestParam("token") String token) {
        System.err.println(token);
    }
}
