package br.com.ifsp.ifome.validation.validators.implement;

import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.validation.interfaces.RestaurantValidator;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

@Component
public class RestaurantNotRegisteredCnpjValidator implements RestaurantValidator {

    private final RestaurantRepository restaurantRepository;

    public RestaurantNotRegisteredCnpjValidator(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Optional<Map.Entry<String, String>> isValid(RestaurantRequest request) {

        boolean exists = restaurantRepository.existsByCnpj(request.cnpj());
        if(exists) {
            return Optional.of(new AbstractMap.SimpleEntry<>("cnpj", "Cnpj já cadastrado"));
        }

        return Optional.empty();
    }
}
