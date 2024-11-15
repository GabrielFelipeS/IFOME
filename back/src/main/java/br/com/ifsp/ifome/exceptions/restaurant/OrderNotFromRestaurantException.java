package br.com.ifsp.ifome.exceptions.restaurant;

public class OrderNotFromRestaurantException extends RuntimeException {
    public OrderNotFromRestaurantException(String message) {
        super(message);
    }
}
