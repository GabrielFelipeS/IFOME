package br.com.ifsp.ifome.validation.validators;

import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidatorRestaurant implements ConstraintValidator<NotRegisteredEmail, String> {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public EmailValidatorRestaurant(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void initialize(NotRegisteredEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }

        boolean emailExists = restaurantRepository.existsByEmail(email);

        if (emailExists) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            return false;
        }

        return true;
    }
}
