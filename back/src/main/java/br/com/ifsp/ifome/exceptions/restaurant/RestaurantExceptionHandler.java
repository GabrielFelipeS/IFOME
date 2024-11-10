package br.com.ifsp.ifome.exceptions.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestaurantExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantExceptionHandler.class);

    @ExceptionHandler(RestaurantNotFoundException.class)
    public  ResponseEntity<Map<String, Object>>  handleRestaurantNotFoundException(
        RestaurantNotFoundException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(OrderNotFromRestaurantException.class)
    public  ResponseEntity<Map<String, Object>>  handleOrderNotFromRestaurantException(
        OrderNotFromRestaurantException ex) {
        logger.warn(ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
