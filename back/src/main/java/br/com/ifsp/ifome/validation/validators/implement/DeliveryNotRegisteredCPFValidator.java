package br.com.ifsp.ifome.validation.validators.implement;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.validation.interfaces.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("clientValidator")
public class DeliveryNotRegisteredCPFValidator implements Validator<DeliveryPersonRequest> {

    private final DeliveryPersonRepository deliveryPersonRepository;

    public DeliveryNotRegisteredCPFValidator(DeliveryPersonRepository deliveryPersonRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    @Override
    public Optional<Map.Entry<String, String>> isValid(DeliveryPersonRequest request) {

        boolean exists = deliveryPersonRepository.existsByCpf(request.cpf());
        if(exists) {
            return Optional.of(new AbstractMap.SimpleEntry<>("cpf", "Cpf já cadastrado"));
        }

        return Optional.empty();
    }
}
