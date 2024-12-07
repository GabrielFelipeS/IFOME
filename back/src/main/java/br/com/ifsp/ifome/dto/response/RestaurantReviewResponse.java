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
        List<ReviewResponse> restaurantReview,
        Double rating
) {
    public static RestaurantReviewResponse from(Restaurant restaurant) {
        return new RestaurantReviewResponse(
                restaurant.getId(),
                restaurant.getNameRestaurant(),
                restaurant.getCnpj(),
                restaurant.getFoodCategory(),
                restaurant.getAddress(),
                restaurant.getTelephone(),
                restaurant.getDishesResponse(),
                restaurant.getOpeningHours(),
                restaurant.getPersonResponsible(),
                restaurant.getPersonResponsibleCpf(),
                restaurant.getEmail(),
                restaurant.getPaymentMethods(),
                restaurant.getRestaurantImage(),
                restaurant.getBankAccount(),
                restaurant.isOpen(),
                restaurant.getRestaurantReviewResponse(),
                restaurant.getRating()

        );
    }
}
