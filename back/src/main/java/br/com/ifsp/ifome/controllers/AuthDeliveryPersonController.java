package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.aspect.Login;
import br.com.ifsp.ifome.aspect.SensitiveData;
import br.com.ifsp.ifome.docs.DocsCreateDeliveryPerson;
import br.com.ifsp.ifome.docs.DocsDeliveryLogin;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.services.AuthDeliveryPersonService;
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

@RequestMapping("/api/auth/deliveryPerson")
public class AuthDeliveryPersonController {
    private final AuthDeliveryPersonService authDeliveryPersonService;

    public AuthDeliveryPersonController(AuthDeliveryPersonService authDeliveryPersonService){
        this.authDeliveryPersonService = authDeliveryPersonService;
    }

    @PostMapping
    @SensitiveData
    @DocsCreateDeliveryPerson
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest, UriComponentsBuilder ucb) throws MethodArgumentNotValidException {
        DeliveryPersonResponse deliveryPersonResponse = authDeliveryPersonService.create(deliveryPersonRequest);

        URI locationOfNewDeliveryPerson = ucb
                .path("deliveryPerson/{id}")
                .buildAndExpand(deliveryPersonResponse.id())
                .toUri();

        ApiResponse apiResponse = new ApiResponse("success", deliveryPersonResponse, "Entragador cadastrado com sucesso");
        return ResponseEntity.created(locationOfNewDeliveryPerson).body(apiResponse);
    }

    @Login
    @SensitiveData
    @DocsDeliveryLogin
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest)   {
        LoginResponse loginResponse = authDeliveryPersonService.login(loginRequest);

        ApiResponse apiResponse = new ApiResponse("success", loginResponse, "Entregador logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }
}
