package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.validation.interfaces.ClientValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@Service
public class ClientService {
    private final TokenService tokenService;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorService<ClientRequest> validatorService;

    public ClientService(TokenService tokenService, ClientRepository clientRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder, ValidatorService<ClientRequest> validatorService) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = validatorService;
    }

    public ClientResponse create(ClientRequest clientRequest) throws MethodArgumentNotValidException {
        validatorService.isValid(clientRequest);
        Client client = new Client(clientRequest, bCryptPasswordEncoder);
        client = clientRepository.save(client);
        return new ClientResponse(client);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<Client> client = clientRepository.findByEmail(loginRequest.email());

        tokenService.isLoginIncorrect(client, loginRequest.password(), bCryptPasswordEncoder);

        var jwtValue = tokenService.generateToken(client.orElseThrow().getEmail());

        ClientResponse clientResponse = new ClientResponse(client.orElseThrow());

        return new LoginResponse(clientResponse, jwtValue);
    }
}
