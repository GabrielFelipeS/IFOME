package br.com.ifsp.ifome.events;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class PedidoStatusChangedEvent {
    private Long pedidoId;
    private OrderStatus newStatus;
    private CustomerOrder customerOrder;
}
