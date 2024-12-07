package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.RestaurantReview;

public record ReviewResponse(
        Long id,
        Double stars,
        String comment
) {
    public ReviewResponse(RestaurantReview restaurantReview) {
        this(
                restaurantReview.getId(),
                restaurantReview.getStars(),
                restaurantReview.getComment()
        );
    }
}
