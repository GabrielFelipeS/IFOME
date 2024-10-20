package br.com.ifsp.ifome.exceptions;

public class DishFromAnotherRestaurant extends RuntimeException{
    public DishFromAnotherRestaurant() {
        super("Só pde ser incluido pratos do mesmo restaurante no pedido");
    }
}
