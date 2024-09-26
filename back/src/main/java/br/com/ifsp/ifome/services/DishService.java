package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.repositories.DishRepository;
import org.springframework.stereotype.Service;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository){
        this.dishRepository = dishRepository;
    }

    public DishResponse create(DishRequest dishRequest){
        Dish dish = new Dish(dishRequest);
        dish = dishRepository.save(dish);
        return new DishResponse(dish);
    }
}
