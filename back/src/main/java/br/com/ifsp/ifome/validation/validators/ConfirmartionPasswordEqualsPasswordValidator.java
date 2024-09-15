package br.com.ifsp.ifome.validation.validators;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.validation.anotations.ConfirmartionPasswordEqualsPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmartionPasswordEqualsPasswordValidator implements ConstraintValidator<ConfirmartionPasswordEqualsPassword, Object> {
    @Override
    public void initialize(ConfirmartionPasswordEqualsPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if(object == null) return false;

        String password = "";
        String confirmationPassword = "";

        if(object instanceof ClientRequest clientRequest) {
            password = clientRequest.password();
            confirmationPassword = clientRequest.confirmationPassword();
        } else if(object instanceof RestaurantRequest restaurantRequest) {
            password = restaurantRequest.password();
            confirmationPassword = restaurantRequest.confirmationPassword();
        } else {
            return false;
        }

        return password.equals(confirmationPassword);
    }
}
