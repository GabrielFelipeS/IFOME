package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.ClientLoginRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.response.ClientLoginResponse;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.repositories.ClientRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
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


        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
            .issuer("api_ifome")
            .subject(client.get().getEmail())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .build()
            ;

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        ClientResponse clientResponse = new ClientResponse(client.orElseThrow());

        return new ClientLoginResponse(clientResponse, jwtValue);
    }
}
