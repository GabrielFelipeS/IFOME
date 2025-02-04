package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.repositories.ClientRepository;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements ConstraintValidator<NotRegisteredEmail, String> {

    private final ClientRepository clientRepository;

    @Autowired
    public EmailValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void initialize(NotRegisteredEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }

        boolean emailExists = clientRepository.existsByEmailIgnoreCase(email);

        if (emailExists) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            return false;
        }

        return true;
    }
}
