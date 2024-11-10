package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.validation.interfaces.Validator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

public class ValidatorService<T> {
    private final List<Validator<T>> validators;

    public ValidatorService(List<Validator<T>> validators) {
        this.validators = validators;
    }

    /**
     * Recebe um request a ser validado, valida baseado no atributo validators.
     * Caso alguma validação de validators falhe lançará uma exceção
     *
     * @param request Request a ser validado
     * @throws MethodArgumentNotValidException Lança a exceção caso uma validação falhe
     */
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
