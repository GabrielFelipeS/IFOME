package br.com.ifsp.ifome.exceptions.client;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException() {
        super("Cliente não encontrado");
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
