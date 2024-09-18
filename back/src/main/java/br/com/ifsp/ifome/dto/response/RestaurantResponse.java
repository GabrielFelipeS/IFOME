package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.BankAccount;
import br.com.ifsp.ifome.entities.Restaurant;

import java.util.List;

public record RestaurantResponse(
        Long id,
        String nameRestaurant,
        String cnpj,
        String foodCategory,
        List<Address> address,
        String telephone,

        String openingHoursStart,
        String openingHoursEnd,

        String personResponsible,
        String personResponsibleCPF,
        String email,
        String paymentMethods,
        String restaurantImage,
        BankAccount bankAccount

) {
    public RestaurantResponse(Restaurant restaurant){
        this( restaurant.getId(),
                restaurant.getNameRestaurant(),
                restaurant.getCnpj(),
                restaurant.getFoodCategory(),
                restaurant.getAddress(),
                restaurant.getTelephone(),

                restaurant.getOpeningHoursStart(),
                restaurant.getOpeningHoursEnd(),

                restaurant.getPersonResponsible(),
                restaurant.getPersonResponsibleCPF(),
                restaurant.getEmail(),
                restaurant.getPaymentMethods(),
                restaurant.getRestaurantImage(),
                restaurant.getBankAccount());
    }
}
