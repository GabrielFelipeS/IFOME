package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;

import java.util.List;

public record RestaurantReviewResponse(
        Long id,
        String nameRestaurant,
        String cnpj,
        String foodCategory,
        List<Address> address,
        String telephone,
        List<DishResponse> dish,
        List<OpeningHours> openingHours,
        String personResponsible,
        String personResponsibleCPF,
        String email,
        String paymentMethods,
        String restaurantImage,
        BankAccount bankAccount,
        boolean isOpen,
        Double rating,
        Double stars,
        String comment

) {
    public static RestaurantReviewResponse from(Restaurant restaurant, RestaurantReview restaurantReview) {
        return new RestaurantReviewResponse( restaurant.getId(),
                restaurant.getNameRestaurant(),
                restaurant.getCnpj(),
                restaurant.getFoodCategory(),
                restaurant.getAddress(),
                restaurant.getTelephone(),
                restaurant.getDishes().stream()
                        .map(DishResponse::new)
                        .toList(),
                restaurant.getOpeningHours(),
                restaurant.getPersonResponsible(),
                restaurant.getPersonResponsibleCpf(),
                restaurant.getEmail(),
                restaurant.getPaymentMethods(),
                restaurant.getRestaurantImage(),
                restaurant.getBankAccount(),
                restaurant.isOpen(),
                restaurant.getRating(),
                restaurantReview.getStars(),
                restaurantReview.getComment()

        );
    }
}
