package br.com.ifsp.ifome.exceptions.global;

import br.com.ifsp.ifome.exceptions.delivery.CoordinatesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.*;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<Map<String, Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = getNameWithError(error);
            List<String> errorMessage = new LinkedList<>(Collections.singletonList(error.getDefaultMessage()));
            addContraintViolation(errorsMap, name, errorMessage);
            logger.warn(error.getDefaultMessage());
        });
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erro ao realizar operação");
        response.put("errors", errorsMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public  ResponseEntity<Map<String, Object>>  handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex) {
        logger.warn(ex.getMessage());
        String message =  ex.getMessage();

        Map<String, Object> response = new HashMap<>();
        response.put("message", message.substring(0, message.indexOf(":")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public  ResponseEntity<Map<String, Object>>  handleMissingServletRequestPart(
        MissingServletRequestPartException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public  ResponseEntity<Map<String, Object>>  handleMaxUploadSizeExceededPart(
        MaxUploadSizeExceededException ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public  ResponseEntity<Map<String, Object>>  handleMaxUploadSizeExceededPart(
        PropertyReferenceException ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CoordinatesException.class)
    public  ResponseEntity<Map<String, Object>>  handleHttpMessageNotReadable(
        CoordinatesException ex) {
        logger.warn(ex.getMessage());
        String message =  ex.getMessage();

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CannotAccessTheChatException.class)
    public  ResponseEntity<Map<String, Object>>  handleCannotAccessTheChat(
        CannotAccessTheChatException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DeliveryChatCannotBeOpenedException.class)
    public  ResponseEntity<Map<String, Object>>  handleCannotAccessTheChat(
        DeliveryChatCannotBeOpenedException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
