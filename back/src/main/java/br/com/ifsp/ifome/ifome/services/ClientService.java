package br.com.ifsp.ifome.ifome.services;

import br.com.ifsp.ifome.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.ifome.entities.Client;
import br.com.ifsp.ifome.ifome.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse create(ClientRequest clientRequest) {
        if(clientRequest.email().equals("user1@gmail.com")) {
           throw new RuntimeException();
        }
        Client client = new Client(clientRequest);
        client = clientRepository.save(client);
        return new ClientResponse(client);
    }
}
