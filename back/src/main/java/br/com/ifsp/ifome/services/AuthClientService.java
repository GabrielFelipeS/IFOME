package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.SensitiveData;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.dto.response.LoginResponse;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class AuthClientService {
    private final LoginService loginService;
    private final ClientRepository clientRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorService<ClientRequest> validatorService;
    private final EmailService emailService;

    // TODO refatorar, diminuir dependencias
    public AuthClientService(TokenService tokenService,
                             ClientRepository clientRepository,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             List<Validator<ClientRequest>> validators,
                             LoginService loginService, EmailService emailService) {
        this.tokenService = tokenService;
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validatorService = new ValidatorService<>(validators);
        this.loginService = loginService;
        this.emailService = emailService;
    }

    /**
     * Cria um cliente com as informações passadas como parâmetro, utiliza o validatorService para fazer validações complementares
     *
     * @param clientRequest Informações do cliente a ser criado
     * @return Cliente criado
     * @throws MethodArgumentNotValidException Caso alguma validação falhe
     */
    @SensitiveData
    public ClientResponse create(ClientRequest clientRequest) throws MethodArgumentNotValidException {
        validatorService.isValid(clientRequest);

        Client client = clientRepository.save(new Client(clientRequest, bCryptPasswordEncoder));

        return new ClientResponse(client);
    }

    /**
     * Tenta realizar o login com email e senha passado, caso algum deles esteja inválido lança {@code BadCredentialsException}
     *
     * @param loginRequest Informações de login, como email e senha
     * @return Informações de login bem sucedido, como informações do cliente e token de validação
     * @throws BadCredentialsException Caso as credenciais estejam incorretas
     */
    @SensitiveData
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<Client> clientOptional = clientRepository.findByEmail(loginRequest.email());

        loginService.isLoginIncorrect(clientOptional, loginRequest.password(), bCryptPasswordEncoder);

        var client = clientOptional.orElseThrow();
        var jwtValue = tokenService.generateToken(client.getEmail(), client.getAuthorities());

        ClientResponse clientResponse = new ClientResponse(client);

        return new LoginResponse(clientResponse, jwtValue);
    }

    public void forgotPassword(HttpServletRequest request, String email) throws Exception {
        Optional<Client> client = clientRepository.findByEmail(email);

        if(client.isEmpty()) return;

        String token = loginService.generateTokenForgotPassword(client.get());

        String body = String.format("link: http://%s:%d/api/auth/client/change_password?token=%s", request.getServerName(), request.getServerPort(), token);

        emailService.sendEmail(email,
            "Redefinição de senha da conta do IFOME",
            body);
    }
}
