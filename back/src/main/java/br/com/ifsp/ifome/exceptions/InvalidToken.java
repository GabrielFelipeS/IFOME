package br.com.ifsp.ifome.exceptions;

public class InvalidToken extends RuntimeException{
    public InvalidToken() {
        super("Token inválido ou expirado");
    }
}
