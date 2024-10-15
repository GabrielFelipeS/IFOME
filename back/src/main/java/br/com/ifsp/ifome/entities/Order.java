package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.OrderRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


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

    @CreationTimestamp
    private LocalDateTime orderDate;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cart cart;

    public Order(Dish dish, Integer quantity, Double price, LocalDateTime orderDate, Cart cart) {
        this(null, dish, quantity, price, orderDate, cart);
    }
}
