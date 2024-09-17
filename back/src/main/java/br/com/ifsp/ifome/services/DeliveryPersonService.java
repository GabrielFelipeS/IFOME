package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryPersonService {
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public DeliveryPersonService(TokenService tokenService, DeliveryPersonRepository deliveryPersonRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.tokenService = tokenService;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public DeliveryPersonResponse create(DeliveryPersonRequest deliveryPersonRequest){
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequest, bCryptPasswordEncoder);
        deliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        return new DeliveryPersonResponse(deliveryPerson);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<DeliveryPerson> deliveryPerson = deliveryPersonRepository.findByEmail(loginRequest.email());
        System.err.println(deliveryPerson.isPresent());
        tokenService.isLoginIncorrect(deliveryPerson, loginRequest.password(), bCryptPasswordEncoder);

        var jwtValue = tokenService.generateToken(deliveryPerson.orElseThrow().getEmail());

        DeliveryPersonResponse deliveryPersonResponse = new DeliveryPersonResponse(deliveryPerson.orElseThrow());

        return new LoginResponse(deliveryPersonResponse, jwtValue);
    }
}
