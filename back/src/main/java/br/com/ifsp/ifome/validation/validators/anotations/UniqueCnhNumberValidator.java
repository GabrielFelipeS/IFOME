package br.com.ifsp.ifome.validation.validators.anotations;


import br.com.ifsp.ifome.repositories.DeliveryPersonRepository;
import br.com.ifsp.ifome.validation.anotations.UniqueCnhNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCnhNumberValidator implements ConstraintValidator<UniqueCnhNumber, String> {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Override
    public boolean isValid(String cnhNumber, ConstraintValidatorContext context) {
        return !deliveryPersonRepository.existsByCnhNumber(cnhNumber);
    }
}
