package br.com.ifsp.ifome.events;

public record EmailSentPedidoStatus(
    String to,
    String subject,
    String body
) { }
