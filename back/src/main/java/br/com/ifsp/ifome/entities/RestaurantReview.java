package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RestaurantReview {

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    public class RestaurantRating {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "restaurant_id", nullable = false)
        private Restaurant restaurant;

        @ManyToOne
        @JoinColumn(name = "customer_order_id", nullable = false, unique = true)
        private CustomerOrder customerOrder;

        @Column(nullable = false)
        private int stars; // Nota de 1 a 5.

        @Column(length = 250)
        private String comment; // Comentário opcional.
    }
}
