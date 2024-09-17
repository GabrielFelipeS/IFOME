package br.com.ifsp.ifome.validation.validators.implement;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.validation.interfaces.ClientValidator;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ClienNotRegisteredCPFValidator implements ClientValidator {

    private final ClientRepository clientRepository;

    public ClienNotRegisteredCPFValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Map.Entry<String, String>> isValid(ClientRequest request) {

        boolean exists = clientRepository.existsByCpf(request.cpf());
        if(exists) {
            return Optional.of(new AbstractMap.SimpleEntry<>("cpf", "Cpf já cadastrado"));
        }

        return Optional.empty();
    }
}
