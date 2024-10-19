package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.client.email = :email AND c.customerOrder IS NULL")
    Optional<Cart> findFirstByClientEmail(@Param("email") String email);
}
