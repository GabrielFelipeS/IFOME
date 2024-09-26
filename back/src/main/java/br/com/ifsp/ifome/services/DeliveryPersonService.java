package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.DeliveryPersonResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.entities.DeliveryPerson;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryPersonService {
    private final LoginService loginService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ValidatorService<DeliveryPersonRequest> validatorService;
    private final EmailService emailService;



    public DeliveryPersonService(TokenService tokenService, DeliveryPersonRepository deliveryPersonRepository,
                                 BCryptPasswordEncoder bCryptPasswordEncoder,
                                 List<Validator<DeliveryPersonRequest>> validators, LoginService loginService, EmailService emailService) {
        this.tokenService = tokenService;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = new ValidatorService<>(validators);
        this.loginService = loginService;
        this.emailService = emailService;

    }
    public DeliveryPersonResponse create(DeliveryPersonRequest deliveryPersonRequest) throws MethodArgumentNotValidException {
        validatorService.isValid(deliveryPersonRequest);
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequest, bCryptPasswordEncoder);
        deliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        return new DeliveryPersonResponse(deliveryPerson);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<DeliveryPerson> deliveryPerson = deliveryPersonRepository.findByEmail(loginRequest.email());

        loginService.isLoginIncorrect(deliveryPerson, loginRequest.password(), bCryptPasswordEncoder);

        var jwtValue = tokenService.generateToken(deliveryPerson.orElseThrow().getEmail());

        DeliveryPersonResponse deliveryPersonResponse = new DeliveryPersonResponse(deliveryPerson.orElseThrow());

        return new LoginResponse(deliveryPersonResponse, jwtValue);
    }


}
