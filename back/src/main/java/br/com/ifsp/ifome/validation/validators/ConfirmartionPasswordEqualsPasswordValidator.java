package br.com.ifsp.ifome.validation.validators;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.validation.anotations.ConfirmartionPasswordEqualsPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmartionPasswordEqualsPasswordValidator implements ConstraintValidator<ConfirmartionPasswordEqualsPassword, ClientRequest> {
    @Override
    public void initialize(ConfirmartionPasswordEqualsPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ClientRequest clientRequest, ConstraintValidatorContext constraintValidatorContext) {
        String password = clientRequest.password();
        String confirmationPassword = clientRequest.confirmationPassword();

        if(password == null || confirmationPassword == null) {
            return false;
        }

        return password.equals(confirmationPassword);
    }
}
