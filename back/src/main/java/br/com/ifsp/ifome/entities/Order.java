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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;
    private Integer quantity;
    private Double price;

    public Order(OrderRequest orderRequest){
        this.dish = orderRequest.dish();
        this.quantity = orderRequest.quantity();
        this.price = orderRequest.price();
    }
}
