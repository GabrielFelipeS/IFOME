package br.com.ifsp.ifome.exceptions.delivery;

public class DeliveryPersonNotFoundException extends RuntimeException{
    public DeliveryPersonNotFoundException() {
        super("Entregador não encontrado");
    }
}
