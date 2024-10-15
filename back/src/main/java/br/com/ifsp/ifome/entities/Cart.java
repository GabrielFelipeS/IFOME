package br.com.ifsp.ifome.entities;


import br.com.ifsp.ifome.dto.request.CartRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryPerson deliveryPerson;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<Order> orders;

    private Double deliveryFee;
    private String orderStats;

    public Cart(CartRequest cartRequest){
        this.client = cartRequest.client();
        this.restaurant = cartRequest.restaurant();
        this.deliveryPerson = cartRequest.deliveryPerson();
        this.orders = cartRequest.orders();
        this.deliveryFee = cartRequest.deliveryFee();
    }
}
