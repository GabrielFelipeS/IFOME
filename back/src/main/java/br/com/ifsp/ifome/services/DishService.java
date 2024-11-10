package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.entities.Dish;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.exceptions.dish.DishNotFoundException;
import br.com.ifsp.ifome.exceptions.restaurant.RestaurantNotFoundException;
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
import java.util.Collections;
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

    /**
     * Cria um novo prato para o restaurante logado com as informações do {@code dishResquest} e salva a imagem armazenada no {@code multipartFile}
     *
     * @param dishRequest Informações do prato a ser cadastrado
     * @param multipartFile Arquivo de imagem a ser salvo
     * @param email Email do restaurante logado
     * @return Informações do prato cadastrado
     * @throws IOException Caso ocorra uma falha ao salvar o arquivo
     * @throws RestaurantNotFoundException Caso não encontre o restaurante pelo email especificado
     */
    public DishResponse create(DishRequest dishRequest, MultipartFile multipartFile, String email)
        throws IOException {

        Restaurant restaurant = restaurantRepository.findByEmail(email)
            .orElseThrow(() -> new RestaurantNotFoundException("Restaurante não encontrado"));

        String imageUrl = fileStorageService.storeFile(restaurant.getCnpj(), multipartFile);

        Dish dish = new Dish(dishRequest, imageUrl);
        dish.setRestaurant(restaurant); // Associe o prato ao restaurante
        dish = dishRepository.save(dish);
        return new DishResponse(dish);
    }

    /**
     * Busca todos os pratos disponíveis
     *
     * @return Lista de todos os pratos disponíveis
     */
    public List<DishResponse> getAllAvailable() {
        return this.dishRepository
            .findAllAvailable(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(DishResponse::new)
            .toList()
            ;
    }

    /**
     * Busca todos os pratos disponíveis, respeitando a paginação passada como parâmetro.
     *
     * @param pageable Paginação, contém a página, a quantidade por página e a ordenação
     * @return Retorna a página, contendo todos os pratos disponíveis dela
     */
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

    /**
     * Busca todos os pratos disponíveis de um restaurante pelo id
     *
     * @param resutaurantId id do restaurante
     * @return Lista de pratos
     */
    public List<DishResponse> getAllAvailableById(Long resutaurantId) {
        return this.dishRepository
            .findAllByRestaurantId(resutaurantId)
            .stream()
            .map(DishResponse::new)
            .toList()
            ;
    }

    /**
     * Busca informações de um prato disponível pelo id, lança uma exceção caso não encontre.
     *
     * @param dishId id do pato
     * @return Informações do prato
     * @throws DishNotFoundException Caso não encontre um prato disponível com o id passado.
     */
    public DishResponse getAvailableDishById(Long dishId) {
        var dishResponse = this.dishRepository
            .findDishAvailableById(dishId)
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
