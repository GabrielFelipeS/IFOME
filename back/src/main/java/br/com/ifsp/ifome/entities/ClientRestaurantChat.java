package br.com.ifsp.ifome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity

public class ClientRestaurantChat extends Chat {
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
