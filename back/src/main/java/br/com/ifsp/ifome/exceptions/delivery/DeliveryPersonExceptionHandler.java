package br.com.ifsp.ifome.exceptions.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DeliveryPersonExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryPersonExceptionHandler.class);

    @ExceptionHandler(DeliveryPersontNotFoundException.class)
    public  ResponseEntity<Map<String, Object>>  handleDeliveryPersontNotFoundException(
        DeliveryPersontNotFoundException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DeclineNotAvailableException.class)
    public  ResponseEntity<Map<String, Object>>  handleDeclineNotAvailableException(
        DeclineNotAvailableException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
