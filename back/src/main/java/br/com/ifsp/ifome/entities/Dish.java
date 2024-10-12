package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.DishRequest;
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
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String dishCategory;
    private String dishImage;
    private String availability;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Dish(DishRequest dishRequest, String imageUrl) {
        this.name = dishRequest.name();
        this.description = dishRequest.description();
        this.price = dishRequest.price();
        this.dishCategory = dishRequest.dishCategory();
        this.dishImage = imageUrl;
        this.availability = dishRequest.availability();
    }
}
