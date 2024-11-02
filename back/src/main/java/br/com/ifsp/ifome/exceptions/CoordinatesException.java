package br.com.ifsp.ifome.exceptions;

public class CoordinatesException extends RuntimeException{
    public CoordinatesException() {
        super("Problema ao pegar coordenadas");
    }
}
