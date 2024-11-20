package br.com.ifsp.ifome.exceptions.global;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Token inválido ou expirado");
    }
}
