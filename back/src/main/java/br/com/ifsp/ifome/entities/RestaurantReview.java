package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.RestaurantReviewRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    public class RestaurantReview {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "restaurant_id", nullable = false)
        private Restaurant restaurant;

        @ManyToOne
        @JoinColumn(name = "customer_order_id", nullable = false, unique = true)
        private CustomerOrder customerOrder;

        @Column(nullable = false)
        private Double stars; // Nota de 1 a 5.

        @Column(length = 250)
        private String comment; // Comentário opcional.

        public RestaurantReview(Restaurant restaurant, CustomerOrder customerOrder, @Valid RestaurantReviewRequest request) {
            this.restaurant = restaurant;
            this.customerOrder = customerOrder;
            this.stars = request.stars();
            this.comment = request.comment();
        }


    }
