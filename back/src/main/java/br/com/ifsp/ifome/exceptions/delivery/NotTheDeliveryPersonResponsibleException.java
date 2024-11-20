package br.com.ifsp.ifome.exceptions.delivery;

public class NotTheDeliveryPersonResponsibleException extends RuntimeException{
    public NotTheDeliveryPersonResponsibleException(String message) {
        super(message);
    }
}
