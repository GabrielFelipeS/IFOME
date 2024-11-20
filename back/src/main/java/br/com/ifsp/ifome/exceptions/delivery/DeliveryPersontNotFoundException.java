package br.com.ifsp.ifome.exceptions.delivery;

public class DeliveryPersontNotFoundException extends RuntimeException{
    public DeliveryPersontNotFoundException() {
        super("Entregador não encontrado");
    }
}
