package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.validation.anotations.CEP;
import br.com.ifsp.ifome.validation.anotations.Past;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

@Component
public class PastValidator implements ConstraintValidator<Past,String> {

    @Override
    public void initialize(Past constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String dateOfBirthStr, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirthStr == null || dateOfBirthStr.isEmpty()) return false;

        try {
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            final LocalDate today = LocalDate.now();
            return today.isAfter(dateOfBirth);
        } catch(DateTimeParseException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
