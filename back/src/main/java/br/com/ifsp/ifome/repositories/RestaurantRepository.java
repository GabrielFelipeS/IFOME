package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCnpj(String email);
    Optional<Restaurant> findByEmail(String email);

    boolean existsByPersonResponsibleCpf(String cpf);

    List<Restaurant> findByNameRestaurantContainingIgnoreCase(String query);
}
