package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Dish;

public record DishResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        Double price,
        String dishCategory,
        String dishImage,
        String priceId,
        String availability

) {
    public DishResponse (Dish dish){
    this(
            dish.getId(),
            dish.getRestaurantId(),
            dish.getName(),
            dish.getDescription(),
            dish.getPrice(),
            dish.getDishCategory(),
            dish.getDishImage(),
            dish.getPriceId(),
            dish.getAvailability()
    );
    }
}
