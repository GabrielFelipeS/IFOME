package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.dish.DishNotFoundException;
import br.com.ifsp.ifome.repositories.DishRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    public DishResponse create(DishRequest dishRequest, MultipartFile multipartFile, Principal principal)
        throws MethodArgumentNotValidException, IOException {

        Restaurant restaurant = restaurantRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        String imageUrl = fileStorageService.storeFile(restaurant.getCnpj(), multipartFile);

        Dish dish = new Dish(dishRequest, imageUrl);
        dish.setRestaurant(restaurant); // Associe o prato ao restaurante
        dish = dishRepository.save(dish);
        return new DishResponse(dish);
    }

    public List<DishResponse> getAllAvailable() {
        return this.dishRepository
            .findAllAvailable(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(DishResponse::new)
            .toList()
            ;
    }

    public Page<DishResponse> getAllAvailable(Pageable pageable) {
        return this.dishRepository
            .findAllAvailable(PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSortOr(Sort.by(Sort.Direction.ASC,"name"))
                ))
            .map(DishResponse::new)
            ;
    }

    public List<DishResponse> getAllAvailableById(Long id) {
        return this.dishRepository
            .findAllByRestaurantId(id)
            .stream()
            .map(DishResponse::new)
            .toList()
            ;
    }

    public DishResponse getAvailableDishById(Long id) {
        var dishResponse = this.dishRepository
            .findDishAvailableById(id)
            .orElseThrow(DishNotFoundException::new)
            ;
        return new DishResponse(dishResponse);
    }


    /**
     * Busca o prato disponível baseado no id, lança uma exceção caso não encontre
     *
     * @param dishId Id do prato
     * @return Objeto Dish, contendo informações do prato
     * @throws DishNotFoundException Caso não encontre um prato disponível com o id passado.
     */
    public Dish getDishAvailableOrElseThrow(Long dishId) {
        Optional<Dish> optionalDish = dishRepository.findDishAvailableById(dishId);
        return optionalDish.orElseThrow(DishNotFoundException::new);
    }

    /**
     * Verifica a existencia de um prato baseado no id, lança uma exceção caso não exista
     *
     * @param dishId Id do prato
     * @throws DishNotFoundException Caso não encontre um prato disponível com o id passado.
     */
    public void dishIdExistsOrElseThrow(Long dishId) {
        boolean exists = dishRepository.existsById(dishId);

        if(!exists) {
            throw new DishNotFoundException();
        }
    }
}
