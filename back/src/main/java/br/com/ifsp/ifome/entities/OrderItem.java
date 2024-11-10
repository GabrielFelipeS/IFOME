package br.com.ifsp.ifome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;
    private Integer quantity;
    private Double unitPrice;
    private String detail;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public OrderItem(Dish dish, Integer quantity, String detail, Cart cart) {
        this(null, dish, quantity, dish.getPrice(), detail, cart);
    }

    public double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public Long getRestaurantId() {
        return this.dish.getRestaurantId();
    }

    public Long getDishId() {
        return this.dish.getId();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderItem orderItem = (OrderItem) object;
        return Objects.equals(dish.getId(), orderItem.dish.getId()) &&  Objects.equals(dish, orderItem.dish) && Objects.equals(cart, orderItem.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dish.getId(), cart.getId());
    }


}
