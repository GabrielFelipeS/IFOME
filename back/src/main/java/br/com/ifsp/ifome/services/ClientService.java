package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.ClientLoginRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.repositories.ClientRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ClientResponse create(ClientRequest clientRequest) {
        Client client = new Client(clientRequest, bCryptPasswordEncoder);
        System.out.println(client.getPassword());
        client = clientRepository.save(client);
        return new ClientResponse(client);
    }

    public ClientLoginResponse login(ClientLoginRequest clientLoginRequest) {
        Optional<Client> client = clientRepository.findByEmail(clientLoginRequest.email());

        if(client.isEmpty() || client.get().isLoginIncorrect(clientLoginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("email or password is invalid!");
        }



        return client.orElseThrow(RuntimeException::new);
        return new ClientLoginResponse(clientResponse, jwtValue);
    }
}
