package br.com.ifsp.ifome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ClientRestaurantChat extends Chat {
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public ClientRestaurantChat(CustomerOrder customerOrder) {
        super(customerOrder);
        this.client = customerOrder.getClient();
        this.restaurant = customerOrder.getRestaurant();
    }
}
