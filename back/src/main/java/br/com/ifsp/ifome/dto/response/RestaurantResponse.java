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

        List<OpeningHours> openingHours,

        String personResponsible,
        String personResponsibleCPF,
        String email,
        String paymentMethods,
        String restaurantImage,
        BankAccount bankAccount,
        boolean isOpen

) {
    public static RestaurantResponse from(Restaurant restaurant) {
            return new RestaurantResponse( restaurant.getId(),
                restaurant.getNameRestaurant(),
                restaurant.getCnpj(),
                restaurant.getFoodCategory(),
                restaurant.getAddress(),
                restaurant.getTelephone(),

                restaurant.getOpeningHours(),

                restaurant.getPersonResponsible(),
                restaurant.getPersonResponsibleCPF(),
                restaurant.getEmail(),
                restaurant.getPaymentMethods(),
                restaurant.getRestaurantImage(),
                restaurant.getBankAccount(),
                restaurant.isOpen());
    }
}
