package br.com.ifsp.ifome.entities;


import br.com.ifsp.ifome.exceptions.DishFromAnotherRestaurant;
import br.com.ifsp.ifome.exceptions.DishNotFoundInCartException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_order_id")
    private CustomerOrder customerOrder;

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

    public Long getIdRestaurant() {
        if(orderItems.isEmpty()) {
            return null;
        }

        return orderItems.get(0).getRestaurantId();
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

    public void updateDishInCart(Long dishId, Integer quantity) {
        Optional<OrderItem> optionalOrderItem = this.orderItems.stream()
                            .filter(orderItem -> Objects.equals(orderItem.getDishId(), dishId))
                            .findFirst();

        if(optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();
            orderItem.setQuantity(quantity);
        }
    }

    public void removeDishBy(Long dishId) {
        Optional<OrderItem> optionalOrderItem = this.orderItems.stream()
            .filter(orderItem -> Objects.equals(orderItem.getDishId(), dishId))
            .findFirst();

        if(optionalOrderItem.isPresent()) {
            System.err.println("AQUI");
            OrderItem orderItem = optionalOrderItem.get();
            this.orderItems.remove(orderItem);
        } else {
            throw new DishNotFoundInCartException();
        }
    }
}
