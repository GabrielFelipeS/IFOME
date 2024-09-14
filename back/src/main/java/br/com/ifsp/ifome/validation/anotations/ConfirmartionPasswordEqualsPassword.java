package br.com.ifsp.ifome.validation.anotations;

import br.com.ifsp.ifome.validation.validators.ConfirmartionPasswordEqualsPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConfirmartionPasswordEqualsPasswordValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmartionPasswordEqualsPassword {
    String message() default "Senha e confirmar senha não são iguais";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
