package br.com.ifsp.ifome.infra;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = getNameWithError(error);
            List<String> errorMessage = new LinkedList<>(Collections.singletonList(error.getDefaultMessage()));
            addContraintViolation(errors, name, errorMessage);
        });
        return errors;
    }

    private String getNameWithError(ObjectError error) {
        if(error instanceof FieldError fieldError) {
            return fieldError.getField();
        }

        return error.getObjectName();
    }

    private void addContraintViolation( Map<String, List<String>> errors, String fieldName, List<String> errorMessage) {
        if (errors.containsKey(fieldName)) {
            errors.merge(fieldName, errorMessage, (value, newValue) -> newValue.addAll(value) ? newValue : value);
        }else {
            errors.put(fieldName, errorMessage);
        }
    }
}
