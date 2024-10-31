package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.SensiveData;
import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class AuthDeliveryPersonService {
    private final LoginService loginService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ValidatorService<DeliveryPersonRequest> validatorService;
    private final EmailService emailService;



    public AuthDeliveryPersonService(TokenService tokenService, DeliveryPersonRepository deliveryPersonRepository,
                                     BCryptPasswordEncoder bCryptPasswordEncoder,
                                     List<Validator<DeliveryPersonRequest>> validators, LoginService loginService, EmailService emailService) {
        this.tokenService = tokenService;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = new ValidatorService<>(validators);
        this.loginService = loginService;
        this.emailService = emailService;

    }

    @SensiveData
    public DeliveryPersonResponse create(DeliveryPersonRequest deliveryPersonRequest) throws MethodArgumentNotValidException {
        validatorService.isValid(deliveryPersonRequest);
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequest, bCryptPasswordEncoder);
        deliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        return new DeliveryPersonResponse(deliveryPerson);
    }

    @SensiveData
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepository.findByEmail(loginRequest.email());

        loginService.isLoginIncorrect(deliveryPersonOptional, loginRequest.password(), bCryptPasswordEncoder);
        DeliveryPerson deliveryPerson = deliveryPersonOptional.orElseThrow();

        var jwtValue = tokenService.generateToken(deliveryPerson.getEmail(), deliveryPerson.getAuthorities());

        DeliveryPersonResponse deliveryPersonResponse = new DeliveryPersonResponse(deliveryPerson);

        return new LoginResponse(deliveryPersonResponse, jwtValue);
    }


}
