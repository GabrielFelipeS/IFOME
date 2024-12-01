package br.com.ifsp.ifome.exceptions.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ClientExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClientNotFound(
        ClientNotFoundException ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DishFromAnotherRestaurant.class)
    public ResponseEntity<Map<String, Object>> handleDishFromAnotherRestaurant(
        DishFromAnotherRestaurant ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DishNotFoundInCartException.class)
    public ResponseEntity<Map<String, Object>> handleDishNotFoundInCart(
        DishNotFoundInCartException ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CartCannotBeEmptyException.class)
    public ResponseEntity<Map<String, Object>> handleCartCannotBeEmptyException(
        CartCannotBeEmptyException ex) {
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message",  ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> hadleOrderNotFoundException(
            OrderNotFoundException ex){
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(OrderNotOwnedByClientException.class)
    public ResponseEntity<Map<String, Object>> OrderNotOwnedByClientException(
            OrderNotOwnedByClientException ex){
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(OrderNotDeliveredException.class)
    public ResponseEntity<Map<String, Object>> OrderNotDeliveredException(
            OrderNotDeliveredException ex){
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(OrderAlreadyReviewedException.class)
    public ResponseEntity<Map<String, Object>> OrderAlreadyReviewedException(
            OrderAlreadyReviewedException ex){
        logger.warn(ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



}
