package br.com.ifsp.ifome.validation.validators;

import br.com.ifsp.ifome.validation.anotations.MinAgeToUse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinAgeToUseValidator implements ConstraintValidator<MinAgeToUse, LocalDate> {
    private int minAgeValue;

    @Override
    public void initialize(MinAgeToUse constraintAnnotation) {
        this.minAgeValue = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        if(dateOfBirth == null) return false;

        final LocalDate today = LocalDate.now();
        final Period period = Period.between(dateOfBirth, today);
        return period.getYears() >= minAgeValue;
    }
}
