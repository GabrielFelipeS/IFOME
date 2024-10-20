package br.com.ifsp.ifome.exceptions;

public class DishNotFoundException extends RuntimeException{
    public DishNotFoundException() {
        super("Prato não encontrado");
    }

    public DishNotFoundException(String message) {
        super(message);
    }
}
