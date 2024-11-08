package br.com.ifsp.ifome.exceptions.restaurant;

public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException() {
        super("Restaurante não encontrado");
    }
}
