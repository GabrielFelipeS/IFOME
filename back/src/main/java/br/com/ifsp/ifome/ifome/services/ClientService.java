package br.com.ifsp.ifome.ifome.services;

import br.com.ifsp.ifome.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.ifome.dto.response.ClientResponse;
import br.com.ifsp.ifome.ifome.entities.Client;
import br.com.ifsp.ifome.ifome.exceptions.EmailException;
import br.com.ifsp.ifome.ifome.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse create(ClientRequest clientRequest) {
        Client client = new Client(clientRequest);
        try {
            client = clientRepository.save(client);
            return new ClientResponse(client);
        } catch(Exception e) {
            throw new EmailException();
        }
    }
}
