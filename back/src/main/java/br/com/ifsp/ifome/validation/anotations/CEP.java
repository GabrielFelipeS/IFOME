package br.com.ifsp.ifome.validation.anotations;

import br.com.ifsp.ifome.validation.validators.anotations.CEPValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {CEPValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^\\d{5}-\\d{3}$")
public @interface CEP {
    String message() default "CEP inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
