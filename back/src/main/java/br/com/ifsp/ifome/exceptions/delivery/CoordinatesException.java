package br.com.ifsp.ifome.exceptions.delivery;

public class CoordinatesException extends RuntimeException{
    public CoordinatesException() {
        super("Problema ao pegar coordenadas");
    }
}
