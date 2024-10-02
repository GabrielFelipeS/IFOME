package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.CnpjValidatorRequest;
import br.com.ifsp.ifome.dto.request.CpfValidatorRequest;
import br.com.ifsp.ifome.dto.request.EmailValidatorRequest;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/validation")
public class ValidationController {

    private final ClientRepository clientRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final RestaurantRepository restaurantRepository;

    public ValidationController(ClientRepository clientRepository, DeliveryPersonRepository deliveryPersonRepository, RestaurantRepository restaurantRepository){
        this.clientRepository = clientRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping("/client/email")
    public ResponseEntity<String> emailClientAlreadyRegistred(@RequestBody @Valid EmailValidatorRequest emailValidatorRequest) {
        boolean alreadyRegistred = clientRepository.existsByEmailIgnoreCase(emailValidatorRequest.email());
        if(alreadyRegistred) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado");
        } else {
            return ResponseEntity.ok("Email não registrado");
        }
    }

    @PostMapping("/client/cpf")
    public ResponseEntity<String> cpfClientAlreadyRegistred(@RequestBody @Valid CpfValidatorRequest cpfValidatorRequest) {
        boolean alreadyRegistred = clientRepository.existsByCpf(cpfValidatorRequest.cpf());
        if(alreadyRegistred) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já registrado");
        } else {
            return ResponseEntity.ok("CPF não registrado");
        }
    }

    @PostMapping("/delivery/email")
    public ResponseEntity<String> emailDeliveryAlreadyRegistred(@RequestBody @Valid EmailValidatorRequest emailValidatorRequest) {
        boolean alreadyRegistred = deliveryPersonRepository.existsByEmailIgnoreCase(emailValidatorRequest.email());
        if(alreadyRegistred) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado");
        } else {
            return ResponseEntity.ok("Email não registrado");
        }
    }

    @PostMapping("/delivery/cpf")
    public ResponseEntity<String> cpfDeliveryAlreadyRegistred(@RequestBody @Valid CpfValidatorRequest cpfValidatorRequest) {
        boolean alreadyRegistred = deliveryPersonRepository.existsByCpf(cpfValidatorRequest.cpf());
        if(alreadyRegistred) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já registrado");
        } else {
            return ResponseEntity.ok("CPF não registrado");
        }
    }

    @PostMapping("/restaurant/email")
    public ResponseEntity<String> emaiRestaurantAlreadyRegistred(@RequestBody @Valid EmailValidatorRequest emailValidatorRequest) {
        boolean alreadyRegistred = restaurantRepository.existsByEmailIgnoreCase(emailValidatorRequest.email());
        if(alreadyRegistred) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado");
        } else {
            return ResponseEntity.ok("Email não registrado");
        }
    }

    @PostMapping("/restaurant/cnpj")
    public ResponseEntity<String> cnpjRestaurantAlreadyRegistred(@RequestBody @Valid CnpjValidatorRequest cnpjValidatorRequest) {
        boolean alreadyRegistred = restaurantRepository.existsByCnpj(cnpjValidatorRequest.cnpj());
        if(alreadyRegistred) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cnpj já registrado");
        } else {
            return ResponseEntity.ok("Cnpj não registrado");
        }
    }



}
