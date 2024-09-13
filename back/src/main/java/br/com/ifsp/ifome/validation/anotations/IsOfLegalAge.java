package br.com.ifsp.ifome.validation.anotations;

import br.com.ifsp.ifome.validation.validators.IsOfLegalAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsOfLegalAgeValidator.class})
public @interface IsOfLegalAge {
    String message() default "É necessário ter mais de 18 anos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
