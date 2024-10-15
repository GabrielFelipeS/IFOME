package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.OrderRequest;
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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Integer quantity;
    private Double price;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cart cart;

    public Order(Dish dish, Integer quantity, Double price, Cart cart) {
        this(null, dish, quantity, price, cart);
    }
}
