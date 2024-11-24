package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.RestaurantReviewRequest;
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
        @JoinColumn(name = "restaurant_id", nullable = false)
        private Restaurant restaurant;

        @ManyToOne
        @JoinColumn(name = "customer_order_id", nullable = false, unique = true)
        private CustomerOrder customerOrder;

        @Column(nullable = false)
        private double stars; // Nota de 1 a 5.

        @Column(length = 250)
        private String comment; // Comentário opcional.

        public static RestaurantReview create(Restaurant restaurant, CustomerOrder order, @Valid RestaurantReviewRequest request) {
            RestaurantReview review = new RestaurantReview();
            review.setRestaurant(restaurant);
            review.setCustomerOrder(order);
            review.setStars(request.stars());
            review.setComment(request.comment());
            return review;
        }


    }
