package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private long price;
    private String dishCategory;
    private String dishImage;
    private String availability;


    public Dish() {}

    public Dish(DishRequest dishRequest) {
        this.id = dishRequest.id;
        this.name = dishRequest.name;
        this.price = dishRequest.price;
        this.dishCategory = dishRequest.dishCategory;
        this.dishImage = dishRequest.dishImage;
        this.availability = dishRequest.availability;
    }

    public Dish(Long id, String name, long price, String dishCategory, String dishImage, String availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dishCategory = dishCategory;
        this.dishImage = dishImage;
        this.availability = availability;
    }
}
