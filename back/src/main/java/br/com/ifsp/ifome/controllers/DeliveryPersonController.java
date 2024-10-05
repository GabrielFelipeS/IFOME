package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.docs.DocsCreateDeliveryPerson;
import br.com.ifsp.ifome.docs.DocsDeliveryLogin;
import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.services.DeliveryPersonService;
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
public class DeliveryPersonController {
    private final DeliveryPersonService deliveryPersonService;

    public DeliveryPersonController(DeliveryPersonService deliveryPersonService){
        this.deliveryPersonService = deliveryPersonService;
    }
    @DocsCreateDeliveryPerson
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest, UriComponentsBuilder ucb) throws MethodArgumentNotValidException {
        DeliveryPersonResponse deliveryPersonResponse = deliveryPersonService.create(deliveryPersonRequest);
        URI locationOfNewDeliveryPerson = ucb
                .path("deliveryPerson/{id}")
                .buildAndExpand(deliveryPersonResponse.id())
                .toUri();

        ApiResponse apiResponse = new ApiResponse("success", deliveryPersonResponse, "Entragador cadastrado com sucesso");
        return ResponseEntity.created(locationOfNewDeliveryPerson).body(apiResponse);
    }

    @PostMapping("/login")
    @DocsDeliveryLogin
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest clientLogin)   {
        LoginResponse loginResponse = deliveryPersonService.login(clientLogin);
        ApiResponse apiResponse = new ApiResponse("success", loginResponse, "Cliente logado com sucesso");
        return ResponseEntity.ok(apiResponse);
    }

}
