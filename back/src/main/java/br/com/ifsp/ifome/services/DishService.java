package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.repositories.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final FileStorageService fileStorageService;

    public DishService(DishRepository dishRepository, FileStorageService fileStorageService){
        this.dishRepository = dishRepository;
        this.fileStorageService = fileStorageService;

    }

    public DishResponse create(DishRequest dishRequest, MultipartFile multipartFile) throws MethodArgumentNotValidException, IOException {

        String imageUrl = fileStorageService.storeFile(dishRequest.name(), multipartFile);

        Dish dish = new Dish(dishRequest, imageUrl);
        dish = dishRepository.save(dish);
        return new DishResponse(dish);
    }
}
