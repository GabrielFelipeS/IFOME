package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.ClientDeliveryChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientDeliveryChatRepository extends JpaRepository<ClientDeliveryChat,Long> {

    @Query("SELECT chat FROM ClientDeliveryChat chat WHERE chat.customerOrder.id = :id")
    Optional<ClientDeliveryChat> findByCustomerOrderId(@Param("id") Long customerOrderId);
}
