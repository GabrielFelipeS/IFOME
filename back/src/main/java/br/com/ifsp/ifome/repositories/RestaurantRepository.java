package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    boolean existsByEmail(String email);
}
