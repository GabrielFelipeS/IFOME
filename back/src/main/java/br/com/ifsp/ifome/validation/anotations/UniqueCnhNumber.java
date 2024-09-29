package br.com.ifsp.ifome.validation.anotations;


import br.com.ifsp.ifome.validation.validators.anotations.UniqueCnhNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueCnhNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCnhNumber {
    String message() default "CNH já registrada";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}