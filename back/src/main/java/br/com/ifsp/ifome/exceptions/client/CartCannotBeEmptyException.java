package br.com.ifsp.ifome.exceptions.client;

public class CartCannotBeEmptyException extends RuntimeException{
    public CartCannotBeEmptyException() {
        super("Selecione pelo menos um prato para continuar.");
    }

    public CartCannotBeEmptyException(String message) {
        super(message);
    }
}
