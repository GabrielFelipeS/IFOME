package br.com.ifsp.ifome.entities;

public enum OrderStatus {
    NOVO,           // Pedido recém-criado
    ACEITO,         // Pedido aceito pelo restaurante
    EM_PREPARO,     // Pedido em preparação
    PRONTO_PARA_ENTREGA, // Pedido pronto para entrega
    SAIU_PARA_ENTREGA,   // Pedido saiu para entrega
    CONCLUIDO       // Pedido concluído
}
