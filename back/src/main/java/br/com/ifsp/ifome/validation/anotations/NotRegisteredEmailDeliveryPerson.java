package br.com.ifsp.ifome.validation.anotations;


import br.com.ifsp.ifome.validation.validators.anotations.EmailValidatorDeliveryPerson;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = EmailValidatorDeliveryPerson.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotRegisteredEmailDeliveryPerson {
    String message() default "E-mail já registrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}