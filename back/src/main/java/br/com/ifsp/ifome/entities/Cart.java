package br.com.ifsp.ifome.entities;


import br.com.ifsp.ifome.dto.request.CartRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Client client;
    private Restaurant restaurant;
    private DeliveryPerson deliveryPerson;
    private List<Order> orders;
    private Double deliveryFee;

    public Cart(CartRequest cartRequest){
        this.client = cartRequest.client();
        this.restaurant = cartRequest.restaurant();
        this.deliveryPerson = cartRequest.deliveryPerson();
        this.orders = cartRequest.orders();
        this.deliveryFee = cartRequest.deliveryFee();
    }
}
