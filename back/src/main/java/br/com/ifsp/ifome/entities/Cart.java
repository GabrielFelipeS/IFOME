package br.com.ifsp.ifome.entities;


import br.com.ifsp.ifome.exceptions.DishFromAnotherRestaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Cart(Client client) {
        this.client = client;
    }

    public void add(OrderItem orderItemToAdd) {
        if(this.cartNotEmpty() && orderItems.stream().noneMatch(orderItem ->
            Objects.equals(orderItem.getRestaurantId(), orderItemToAdd.getRestaurantId()))) {
            throw new DishFromAnotherRestaurant();
        }

        if(this.orderItems.contains(orderItemToAdd)) {
           int index = this.orderItems.indexOf(orderItemToAdd);
           OrderItem orderItem = this.orderItems.get(index);
           orderItem.addQuantity(orderItemToAdd.getQuantity());
           return;
        }

        orderItems.add(orderItemToAdd);
    }

    public Double totalPrice() {
        return this.orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public boolean cartEmpty() {
        return this.orderItems.isEmpty();
    }

    public boolean cartNotEmpty() {
        return !this.cartEmpty();
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }
}
