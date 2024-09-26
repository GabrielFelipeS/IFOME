package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.Dish;

public record DishResponse(
        Long id,
        String name,
        Double price,
        String dishCategory,
        String dishImage,
        String availability
) {
    public DishResponse (Dish dish){
    this(
            dish.getId(),
            dish.getName(),
            dish.getPrice(),
            dish.getDishCategory(),
            dish.getDishImage(),
            dish.getAvailability()
    );
    }
}
