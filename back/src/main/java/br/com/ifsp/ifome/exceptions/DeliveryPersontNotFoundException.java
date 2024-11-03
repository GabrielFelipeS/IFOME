package br.com.ifsp.ifome.exceptions;

public class DeliveryPersontNotFoundException extends RuntimeException{
    public DeliveryPersontNotFoundException() {
        super("Entregador não encontrado");
    }
}
