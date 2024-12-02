package br.com.ifsp.ifome.entities;


import br.com.ifsp.ifome.exceptions.client.CartCannotBeEmptyException;
import br.com.ifsp.ifome.exceptions.client.DishFromAnotherRestaurant;
import br.com.ifsp.ifome.exceptions.client.DishNotFoundInCartException;
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

    private Double freight;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_order_id")
    private CustomerOrder customerOrder;

    public Cart(Client client) {
        this.client = client;
        this.freight = 0.0;
    }

    public void add(OrderItem orderItemToAdd) {
        if(this.cartNotEmpty() && orderItems.stream().noneMatch(orderItem ->
            Objects.equals(orderItem.getRestaurantId(), orderItemToAdd.getRestaurantId()))) {
            throw new DishFromAnotherRestaurant();
        }

        double totalPrice = this.totalPriceWithoutShipping();

        this.freight = totalPrice <= 50? 15.0 : totalPrice <= 100? 10.0 : 0.0;

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


    public Double totalPriceWithoutShipping() {
        return this.orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

// TODO colocar o frete de volta
    public Double totalPrice() {
//        return this.totalPriceWithoutShipping() + freight;
        return this.totalPriceWithoutShipping();
    }

    public boolean cartEmpty() {
        return  this.orderItems == null || this.orderItems.isEmpty();
    }

    public boolean cartNotEmpty() {
        return !this.cartEmpty();
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public String getClientName() {
        return this.client.getName();
    }

    public String getEmailClient() {
        return this.client.getEmail();
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

    public OrderItem removeDishBy(Long dishId) {
        Optional<OrderItem> optionalOrderItem = this.orderItems.stream()
            .peek(orderItem -> System.out.println(orderItem.getCart()))
            .filter(orderItem -> Objects.equals(orderItem.getDishId(), dishId))
            .findFirst();

        if(optionalOrderItem.isEmpty()) {
            throw new DishNotFoundInCartException();
        }


        OrderItem orderItem = optionalOrderItem.get();

        System.err.println(orderItem);
        System.err.println("-----------------");
        System.err.println("Antes de remover");
        this.orderItems.forEach(System.err::println);
        this.orderItems.remove(orderItem);

        orderItem.setCart(null);

        if(orderItems.isEmpty()) {
            this.freight = 0.0;
        } else {
            double totalPrice = this.totalPriceWithoutShipping();
            this.freight = totalPrice <= 50? 15.0 : totalPrice <= 100? 10.0 : 0.0;
        }

        System.err.println("Depois de remover");
        this.orderItems.forEach(System.err::println);
        System.err.println("-----------------");

        return orderItem;
    }

    public Cart cartCannotBeEmpty() {
        if(this.cartEmpty()) {
            throw new CartCannotBeEmptyException();
        }
        return this;
    }

    public Integer totalQuantity() {
        return this.orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public String getClientPhone() {
        return this.client.getPhone();
    }

    public void clearCart() {
        this.orderItems.clear();
    }

}
