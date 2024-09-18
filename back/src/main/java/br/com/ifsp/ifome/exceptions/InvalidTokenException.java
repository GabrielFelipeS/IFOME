package br.com.ifsp.ifome.exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Token inválido ou expirado");
    }
}
