package br.com.ifsp.ifome.exceptions.client;

public class OrderNotDeliveredException extends RuntimeException {
    public OrderNotDeliveredException() {
        super("Apenas pedidos entregues podem ser avaliados.");
    }

    public OrderNotDeliveredException(String message) {
        super(message);
    }
}
