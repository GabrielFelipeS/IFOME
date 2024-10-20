package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.validation.anotations.MaxAgeToUse;
import br.com.ifsp.ifome.validation.anotations.MinAgeToUse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class MaxAgeToUseValidator implements ConstraintValidator<MaxAgeToUse, String> {
    private int maxAgeValue;

    @Override
    public void initialize(MaxAgeToUse constraintAnnotation) {
        this.maxAgeValue = constraintAnnotation.maxAge();
    }

    @Override
    public boolean isValid(String dateOfBirthStr, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirthStr == null || dateOfBirthStr.isEmpty()) return false;

        try {
            final LocalDate today = LocalDate.now();
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            final Period period = Period.between(dateOfBirth, today);
            return period.getYears() <= maxAgeValue;
        } catch(DateTimeParseException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
