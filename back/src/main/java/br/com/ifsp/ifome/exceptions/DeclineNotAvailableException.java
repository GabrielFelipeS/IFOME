package br.com.ifsp.ifome.exceptions;

public class DeclineNotAvailableException extends RuntimeException{
    public DeclineNotAvailableException(String msg) {
        super("Recusar não disponivel para o status: " + msg.replace("_", "").toLowerCase());
    }
}
