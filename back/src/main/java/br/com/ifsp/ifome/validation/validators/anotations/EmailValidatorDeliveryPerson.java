package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmailDeliveryPerson;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidatorDeliveryPerson implements ConstraintValidator<NotRegisteredEmailDeliveryPerson, String> {
    private final DeliveryPersonRepository deliveryPersonRepository;

    @Autowired
    public EmailValidatorDeliveryPerson(DeliveryPersonRepository deliveryPersonRepository){
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    @Override
    public void initialize(NotRegisteredEmailDeliveryPerson constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }

        boolean emailExists = deliveryPersonRepository.existsByEmailIgnoreCase(email);

        if (emailExists) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }

        return true;
    }
}
