package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.aspect.Login;
import br.com.ifsp.ifome.aspect.SensitiveData;
import br.com.ifsp.ifome.docs.DocsClientLogin;
import br.com.ifsp.ifome.docs.DocsCreateClient;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.ForgotPasswordRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.services.AuthClientService;
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
    private final AuthClientService authClientService;

    public AuthClientController(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @SensitiveData
    @PostMapping
    @DocsCreateClient
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ClientRequest clientRequest , UriComponentsBuilder ucb) throws MethodArgumentNotValidException {
        ClientResponse clientResponse = authClientService.create(clientRequest);

        URI locationOfNewClient = ucb
            .path("client/{id}")
            .buildAndExpand(clientResponse.id())
            .toUri();

        ApiResponse response = new ApiResponse("success", clientResponse, "Cliente criado com sucesso");
        return ResponseEntity.created(locationOfNewClient).body(response);
    }

    @Login
    @SensitiveData
    @DocsClientLogin
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authClientService.login(loginRequest);

        ApiResponse apiResponse = new ApiResponse("success", loginResponse, "Cliente logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

    // TODO Terminar isso
    @PostMapping("/forgot_password")
    public void forgotPassword(HttpServletRequest request, @RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        System.err.println(request.getServerName());
        System.out.println(forgotPasswordRequest.email());
        authClientService.forgotPassword(request, forgotPasswordRequest.email());
    }

    @GetMapping("/change_password")
    public void changePassword(@RequestParam("token") String token) {
        System.err.println(token);
    }
}
