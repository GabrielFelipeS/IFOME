package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.validation.interfaces.Validator;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class ValidatorService<T> {
    private final List<? extends Validator<T>> validators;

    public ValidatorService(List<? extends Validator<T>> validators) {
        this.validators = validators;
    }

    public void isValid(T request) throws MethodArgumentNotValidException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, request.getClass().getName());
        validators.stream()
            .map(validator -> validator.isValid(request))
            .filter(Optional::isPresent)
            .forEach(validator ->
                bindingResult.addError(
                    new FieldError(request.getClass().getName(),
                        validator.get().getKey(),
                        validator.get().getValue())
                )
            );

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }
}
