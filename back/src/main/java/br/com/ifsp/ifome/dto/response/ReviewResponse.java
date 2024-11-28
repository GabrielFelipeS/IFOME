package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;

public record ReviewResponse(
        Long id,
        Restaurant restaurant,
        CustomerOrder customerOrder,
        Double stars,
        String comment


) {
    public ReviewResponse(RestaurantReview restaurantReview) {
        this(
                restaurantReview.getId(),
                restaurantReview.getRestaurant(),
                restaurantReview.getCustomerOrder(),
                restaurantReview.getStars(),
                restaurantReview.getComment()

        );
    }
}
