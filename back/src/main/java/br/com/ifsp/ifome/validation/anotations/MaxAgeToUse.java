package br.com.ifsp.ifome.validation.anotations;

import br.com.ifsp.ifome.validation.validators.anotations.MaxAgeToUseValidator;
import br.com.ifsp.ifome.validation.validators.anotations.MinAgeToUseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MaxAgeToUseValidator.class})
public @interface MaxAgeToUse {
    String message() default "Para cadastro no sistema, é necessário ter pelo menos 18 anos de idade.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int maxAge() default 18;
}
