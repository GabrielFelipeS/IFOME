package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.RestaurantReview;

public record ReviewResponse(
        Long id,
        CustomerOrderResponse customerOrder,
        Double stars,
        String comment
) {
    public ReviewResponse(RestaurantReview restaurantReview) {
        this(
                restaurantReview.getId(),
                CustomerOrderResponse.from(restaurantReview.getCustomerOrder()),
                restaurantReview.getStars(),
                restaurantReview.getComment()

        );
    }
}
