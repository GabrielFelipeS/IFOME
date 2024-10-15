package br.com.ifsp.ifome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Integer quantity;
    private Double unitPrice;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Cart cart;

    public OrderItem(Dish dish, Integer quantity, Cart cart) {
        this(null, dish, quantity, dish.getPrice(), cart);
    }
}
