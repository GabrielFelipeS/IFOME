package br.com.ifsp.ifome.exceptions;

public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException() {
        super("Restaurante não encontrado");
    }
}
