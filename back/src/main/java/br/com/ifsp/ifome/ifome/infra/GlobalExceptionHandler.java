package br.com.ifsp.ifome.ifome.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private ResponseEntity<String> constraintViolationHanlder(SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.unprocessableEntity().body(exception.getLocalizedMessage());
    }
}
