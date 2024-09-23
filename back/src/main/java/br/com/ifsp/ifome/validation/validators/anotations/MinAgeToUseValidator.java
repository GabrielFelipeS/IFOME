package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.validation.anotations.MinAgeToUse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class MinAgeToUseValidator implements ConstraintValidator<MinAgeToUse, String> {
    private int minAgeValue;

    @Override
    public void initialize(MinAgeToUse constraintAnnotation) {
        this.minAgeValue = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(String dateOfBirthStr, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirthStr == null || dateOfBirthStr.isEmpty()) return false;

        try {
            final LocalDate today = LocalDate.now();
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            final Period period = Period.between(dateOfBirth, today);
            return period.getYears() >= minAgeValue;
        } catch(DateTimeParseException e) {}

       return false;
    }
}
