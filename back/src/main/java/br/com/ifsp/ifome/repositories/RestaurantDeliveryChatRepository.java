package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.ClientRestaurantChat;
import br.com.ifsp.ifome.entities.RestaurantDeliveryChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestaurantDeliveryChatRepository extends JpaRepository<RestaurantDeliveryChat,Long> {

    @Query("SELECT chat FROM RestaurantDeliveryChat chat WHERE chat.customerOrder.id = :id")
    Optional<RestaurantDeliveryChat> findByCustomerOrderId(@Param("id") Long customerOrderId);
}
