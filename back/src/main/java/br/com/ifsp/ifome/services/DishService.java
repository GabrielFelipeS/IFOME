package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final FileStorageService fileStorageService;
    private final RestaurantRepository restaurantRepository;

    public DishService(DishRepository dishRepository, FileStorageService fileStorageService, RestaurantRepository restaurantRepository){
        this.dishRepository = dishRepository;
        this.fileStorageService = fileStorageService;
        this.restaurantRepository = restaurantRepository;

    }

    public DishResponse create(DishRequest dishRequest, MultipartFile multipartFile, Long restaurantId)
            throws MethodArgumentNotValidException, IOException {

        String imageUrl = fileStorageService.storeFile(dishRequest.name(), multipartFile);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));


        Dish dish = new Dish(dishRequest, imageUrl);
        dish.setRestaurant(restaurant); // Associe o prato ao restaurante
        dish = dishRepository.save(dish);
        return new DishResponse(dish);
    }
}
