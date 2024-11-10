package br.com.ifsp.ifome.exceptions.client;

public class DishNotFoundInCartException extends RuntimeException{
    public DishNotFoundInCartException() {
        super("Prato não encontrado no carrinho");
    }

    public DishNotFoundInCartException(String message) {
        super(message);
    }
}
