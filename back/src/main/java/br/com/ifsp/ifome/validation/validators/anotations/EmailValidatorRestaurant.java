package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmailRestaurant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidatorRestaurant implements ConstraintValidator<NotRegisteredEmailRestaurant, String> {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public EmailValidatorRestaurant(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void initialize(NotRegisteredEmailRestaurant constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }

        boolean emailExists = restaurantRepository.existsByEmailIgnoreCase(email);

        if (emailExists) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            return false;
        }

        return true;
    }
}
