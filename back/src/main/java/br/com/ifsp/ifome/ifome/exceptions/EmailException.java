package br.com.ifsp.ifome.ifome.exceptions;

public class EmailException extends  RuntimeException {
    public EmailException() {
        super("E-mail já registrado");
    }
}
