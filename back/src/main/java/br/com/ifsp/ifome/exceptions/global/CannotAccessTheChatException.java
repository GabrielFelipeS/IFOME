package br.com.ifsp.ifome.exceptions.global;

public class CannotAccessTheChatException extends RuntimeException{
    public CannotAccessTheChatException(String message) {
        super(message);
    }
}
