package br.com.ifsp.ifome.validation.anotations;

import br.com.ifsp.ifome.validation.validators.anotations.CEPValidator;
import br.com.ifsp.ifome.validation.validators.anotations.PastValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {PastValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Past {
    String message() default "Data deve estar no passado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
