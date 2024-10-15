package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findFirstByClientEmail(String email);
}
