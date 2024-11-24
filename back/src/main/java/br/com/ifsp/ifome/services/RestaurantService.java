package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.dto.request.RestaurantReviewRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.Restaurant;
import br.com.ifsp.ifome.entities.RestaurantReview;
import br.com.ifsp.ifome.exceptions.restaurant.RestaurantNotFoundException;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.RestaurantRepository;
import br.com.ifsp.ifome.repositories.RestaurantReviewRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, CustomerOrderRepository customerOrderRepository, RestaurantReviewRepository restaurantReviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.restaurantReviewRepository = restaurantReviewRepository;
    }

    /**
     * Abre/fecha o restaurante baseado no status atual dele
     *
     * @param email Email do restaurante logado
     * @return retorna a mensagem de atualização do restaurante
     * @throws RestaurantNotFoundException Caso não encontre o restaurante com o id especificado
     */
    public String changeStateOpen(String email) {
        var restaurant = restaurantRepository.findByEmail(email).orElseThrow(RestaurantNotFoundException::new);
        restaurant.reverseStatusOpen();
        restaurantRepository.save(restaurant);

        return String.format("Restaurante %s com sucesso!", restaurant.isOpen()? "aberto" : "fechado");
    }

    /**
     * Retorna a lista de restaurantes em ordem alfabetica
     *
     * @return lista de restaurantes
     */
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll(
            Sort.by(
                Sort.Direction.ASC, "nameRestaurant")
            ).stream()
            .map(RestaurantResponse::from)
            .toList();
    }

    /**
     * Retorna a lista de restaurantes em ordem alfabetica
     *
     * @param pageable informações de paginação
     * @return lista de restaurantes
     */
     public Page<RestaurantResponse> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSortOr(Sort.by(Sort.Direction.ASC,"nameRestaurant"))
                )).map(RestaurantResponse::from);
    }

    /**
     * Busca um restaurante pelo id
     *
     * @param id id do restaurante
     * @return restaurante com id passado como parametro
     * @throws RestaurantNotFoundException Caso não encontre o restaurante com o id especificado
     */
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
            .orElseThrow(RestaurantNotFoundException::new);
    }

    /**
     * Busca um restaurante pelo id
     *
     * @param id id do restaurante
     * @return restaurante com id passado como parametro
     * @throws RestaurantNotFoundException Caso não encontre o restaurante com o id especificado
     */
    public RestaurantResponse findRestaurantResponseById(Long id) {
        Restaurant restaurant = this.findById(id);
        return RestaurantResponse.from(restaurant);
    }

    /**
     * Busca todos os pedidos de um restaurante
     *
     * @param email E-mail do restaurante logado
     * @return Lista de pedidos do restaurante
     * @throws RestaurantNotFoundException Caso não encontre o restaurante com o email especificado
     */
    public List<CustomerOrderResponse> getOrders(String email) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByEmail(email);

        Restaurant restaurant = restaurantOpt.orElseThrow(() ->
            new RestaurantNotFoundException("Restaurante não encontrado com o email: " + email)
        );

        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantId(restaurant.getId());

        return orders.stream()
            .map(CustomerOrderResponse::from)
            .toList();
    }

    /**
     *
     * @param restaurantEmail Email do restaurante
     * @return restaurante encontrado com o E-mail
     * @throws RestaurantNotFoundException Caso não encontre o restaurante
     */
    public Restaurant findByEmail(String restaurantEmail) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByEmail(restaurantEmail);
        return restaurantOpt.orElseThrow(() ->
            new RestaurantNotFoundException("Restaurante não encontrado com o email: " + restaurantEmail)
        );
    }


    public void reviewRestaurant(Long orderId, @Valid RestaurantReviewRequest reviewRequest, String customerEmail) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));

        if (!order.isOwnedByClient(customerEmail)) {
            throw new IllegalArgumentException("Pedido não pertence ao cliente autenticado.");
        }

        if (!order.isDelivered()) {
            throw new IllegalArgumentException("Apenas pedidos entregues podem ser avaliados.");
        }

        if (restaurantReviewRepository.existsByCustomerOrder_Id(order.getId())) {
            throw new IllegalArgumentException("Este pedido já foi avaliado.");
        }

        Restaurant restaurant = order.getRestaurant();

        RestaurantReview review = new RestaurantReview(restaurant, order, reviewRequest);
        restaurantReviewRepository.save(review);


        restaurantReviewRepository.save(review);

        // Recalcular média e atualizar no restaurante
        double averageRating = restaurantReviewRepository.calculateAverageRating(restaurant.getId());
        restaurant.updateRating(averageRating);

        restaurantRepository.save(restaurant);
    }

}
