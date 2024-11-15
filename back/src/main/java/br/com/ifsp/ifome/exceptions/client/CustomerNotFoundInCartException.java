package br.com.ifsp.ifome.exceptions.client;

public class CustomerNotFoundInCartException extends RuntimeException{
    public CustomerNotFoundInCartException() {
        super("Pedido não encontrado");
    }

    public CustomerNotFoundInCartException(String message) {
        super(message);
    }
}
