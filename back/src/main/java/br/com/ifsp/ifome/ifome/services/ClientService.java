package br.com.ifsp.ifome.ifome.services;

import br.com.ifsp.ifome.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.ifome.dto.response.ClientResponse;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    public ClientResponse create(ClientRequest clientRequest) {
        return new ClientResponse(1L,
            clientRequest.email(), clientRequest.dateOfBirth(), clientRequest.cpf(),
            clientRequest.typeResidence(), clientRequest.cep(), clientRequest.address(), clientRequest.paymentMethods());
    }
}
