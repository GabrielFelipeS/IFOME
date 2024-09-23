package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.validation.anotations.DateFormat;
import br.com.ifsp.ifome.validation.anotations.Past;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Component
public class DateFormatValidator implements ConstraintValidator<DateFormat,String> {
    @Override
    public void initialize(DateFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String dateOfBirthStr, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirthStr == null || dateOfBirthStr.isEmpty()) return false;

        try {
            LocalDate.parse(dateOfBirthStr);
            return true;
        } catch(DateTimeParseException e) {
            return false;
        }
    }
}
