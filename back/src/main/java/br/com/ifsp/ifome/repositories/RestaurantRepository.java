package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import br.com.ifsp.ifome.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCnpj(String email);
    Optional<Restaurant> findByEmail(String email);
    default List<RestaurantResponse> getAllRestaurants() {
        return this.findAll().stream().map(RestaurantResponse::from).collect(Collectors.toList());
    }
}
