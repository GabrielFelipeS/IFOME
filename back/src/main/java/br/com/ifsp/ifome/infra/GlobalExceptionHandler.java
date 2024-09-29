package br.com.ifsp.ifome.infra;

import br.com.ifsp.ifome.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  Map<String, Object> handleValidationExceptions(

        MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = getNameWithError(error);
            List<String> errorMessage = new LinkedList<>(Collections.singletonList(error.getDefaultMessage()));
            addContraintViolation(errorsMap, name, errorMessage);
            logger.warn(error.getDefaultMessage());
        });
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro ao cadastrar cliente");
        response.put("errors", errorsMap);
        return response;
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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> invalidTokenExceptionHandler(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public  ResponseEntity<Map<String, Object>>  handleBadCredentialExceptions(
        BadCredentialsException ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
