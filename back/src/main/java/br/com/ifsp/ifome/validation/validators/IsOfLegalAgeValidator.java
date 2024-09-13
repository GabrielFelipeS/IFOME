package br.com.ifsp.ifome.validation.validators;

import br.com.ifsp.ifome.validation.anotations.IsOfLegalAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class IsOfLegalAgeValidator implements ConstraintValidator<IsOfLegalAge, LocalDate> {

    @Override
    public void initialize(IsOfLegalAge constraintAnnotation) {}

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        final LocalDate today = LocalDate.now();
        final Period period = Period.between(dateOfBirth, today);
        return period.getYears() >= 18;
    }
}
