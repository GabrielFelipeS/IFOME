package br.com.ifsp.ifome.exceptions.restaurant;

public class RestaurantIsCloseException extends RuntimeException{
    public RestaurantIsCloseException(String message) {
        super(message);
    }
}
