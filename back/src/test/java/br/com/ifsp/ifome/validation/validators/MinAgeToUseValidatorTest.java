package br.com.ifsp.ifome.validation.validators;

import br.com.ifsp.ifome.validation.anotations.MinAgeToUse;
import br.com.ifsp.ifome.validation.validators.anotations.MinAgeToUseValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MinAgeToUseValidatorTest {

    @Mock
    private MinAgeToUse minAgeToUse;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private MinAgeToUseValidator minAgeToUseValidator;

    @BeforeEach
    public void setup() {
        this.minAgeToUseValidator = new MinAgeToUseValidator();
    }

    @Test
    @DisplayName("should return true when age is equal to minimum age and anniversary date is today")
    public void shouldReturnTrueWhenAgeIsEqualMinimumAgeAndAnniversaryDateIsToday() {
        when(minAgeToUse.minAge()).thenReturn(14);

        minAgeToUseValidator.initialize(minAgeToUse);

        boolean isValidMinAge = minAgeToUseValidator.isValid(LocalDate.now().minusYears(14).toString(),constraintValidatorContext);
        assertThat(isValidMinAge).isTrue();
    }

    @Test
    public void shouldBeReturnFalseToNullLocalDate() {
        when(minAgeToUse.minAge()).thenReturn(14);
        minAgeToUseValidator.initialize(minAgeToUse);
        boolean isValidMinAge = minAgeToUseValidator.isValid(null, constraintValidatorContext);
        assertThat(isValidMinAge).isFalse();
    }

    @Test
    @DisplayName("should return false when anniversary date is tomorrow")
    public void shouldReturnFalseWhenAnniversaryDateIsTomorrow() {
        when(minAgeToUse.minAge()).thenReturn(14);
        minAgeToUseValidator.initialize(minAgeToUse);
        LocalDate anniversaryDateIsTommorow = LocalDate.now().minusYears(14).plusDays(1);
        boolean isValidMinAge = minAgeToUseValidator.isValid(anniversaryDateIsTommorow.toString(), constraintValidatorContext);
        assertThat(isValidMinAge).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2011-01-01", "2012-01-01", "2013-01-01", "2024-01-01", "2011-08-14"})
    public void shouldReturnFalseWhenItIsNotOfMinimumAge(String dateOfBirth) {
        when(minAgeToUse.minAge()).thenReturn(14);
        minAgeToUseValidator.initialize(minAgeToUse);
        boolean isValidMinAge = minAgeToUseValidator.isValid(dateOfBirth, constraintValidatorContext);
        assertThat(isValidMinAge).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2010-01-01", "2003-01-01", "1999-02-01", "1990-01-01"})
    public void shouldReturnTrueeWhenItIsMinimumAge(String dateOfBirth) {
        when(minAgeToUse.minAge()).thenReturn(14);
        minAgeToUseValidator.initialize(minAgeToUse);
        boolean isValidMinAge = minAgeToUseValidator.isValid(dateOfBirth, constraintValidatorContext);
        assertThat(isValidMinAge).isTrue();
    }
}
