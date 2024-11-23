package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.ClientRestaurantChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRestaurantChatRepository extends JpaRepository<ClientRestaurantChat,Long> {

    @Query("SELECT chat FROM ClientRestaurantChat chat WHERE chat.customerOrder.id = :id")
    Optional<ClientRestaurantChat> findByCustomerOrderId(@Param("id") Long customerOrderId);
}
