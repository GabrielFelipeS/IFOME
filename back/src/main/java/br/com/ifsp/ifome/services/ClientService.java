package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final LoginService loginService;
    private final TokenService tokenService;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorService<ClientRequest> validatorService;

    public ClientService(TokenService tokenService, ClientRepository clientRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         List<Validator<ClientRequest>> validators, LoginService loginService) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = new ValidatorService<>(validators);
        this.loginService = loginService;
    }

    public ClientResponse create(ClientRequest clientRequest) throws MethodArgumentNotValidException {
        validatorService.isValid(clientRequest);
        Client client = new Client(clientRequest, bCryptPasswordEncoder);
        client = clientRepository.save(client);
        return new ClientResponse(client);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<Client> client = clientRepository.findByEmail(loginRequest.email());

        loginService.isLoginIncorrect(client, loginRequest.password(), bCryptPasswordEncoder);

        var jwtValue = tokenService.generateToken(client.orElseThrow().getEmail());

        ClientResponse clientResponse = new ClientResponse(client.orElseThrow());

        return new LoginResponse(clientResponse, jwtValue);
    }
}
