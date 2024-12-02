package br.com.ifsp.ifome.exceptions.client;

public class OrderNotOwnedByClientException extends RuntimeException {
    public OrderNotOwnedByClientException() {
        super("Pedido não pertence ao cliente autenticado.");
    }

    public OrderNotOwnedByClientException(String message) {
        super(message);
    }
}
