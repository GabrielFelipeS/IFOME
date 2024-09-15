package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.services.DeliveryPersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<DeliveryPersonResponse> create(@Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest, UriComponentsBuilder ucb){
        DeliveryPersonResponse deliveryPersonResponse = deliveryPersonService.create(deliveryPersonRequest);
        URI locationOfNewDeliveryPerson = ucb
                .path("deliveryPerson/{id}")
                .buildAndExpand(1)
                .toUri();
        return ResponseEntity.created(locationOfNewDeliveryPerson).body(deliveryPersonResponse);
    }
}
