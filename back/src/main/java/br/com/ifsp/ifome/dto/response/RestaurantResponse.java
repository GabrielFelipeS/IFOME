package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.BankAccount;
import br.com.ifsp.ifome.entities.OpeningHours;
import br.com.ifsp.ifome.entities.Restaurant;

import java.util.List;

public record RestaurantResponse(
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
        Double rating
) {
    public static RestaurantResponse from(Restaurant restaurant) {
            return new RestaurantResponse( restaurant.getId(),
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
                restaurant.getRating());
    }
}
