package br.com.ifsp.ifome.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ClientDeliveryChat extends Chat {
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryPerson deliveryPerson;

    public ClientDeliveryChat(CustomerOrder customerOrder) {
        super(customerOrder);
        this.client = customerOrder.getClient();
        this.deliveryPerson = customerOrder.getDeliveryPerson();
    }
}
