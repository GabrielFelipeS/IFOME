package br.com.ifsp.ifome.validation.validators.implement;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.validation.interfaces.ClientValidator;

public class ClienNotRegisteredCPFValidator implements ClientValidator {

    private final ClientRepository clientRepository;

    public ClienNotRegisteredCPFValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean isValid(ClientRequest request) {
        return clientRepository.existsByEmailIgnoreCase(request.cpf());
    }
}
