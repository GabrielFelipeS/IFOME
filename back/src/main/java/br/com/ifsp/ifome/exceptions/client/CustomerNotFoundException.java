package br.com.ifsp.ifome.exceptions.client;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException() {
        super("Pedido não encontrado");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
