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
        return true;
    }
}
