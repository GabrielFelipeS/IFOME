package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.DishRequest;
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
    private String priceId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Dish(DishRequest dishRequest, String imageUrl, String priceId) {
        this.name = dishRequest.name();
        this.description = dishRequest.description();
        this.price = dishRequest.price();
        this.dishCategory = dishRequest.dishCategory();
        this.dishImage = imageUrl;
        this.availability = dishRequest.availability();
        this.priceId = priceId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dish dish = (Dish) object;
        return Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getRestaurantId() {
        return this.getRestaurant().getId();
    }
}
