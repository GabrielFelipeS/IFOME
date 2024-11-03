package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>
{
    List<CustomerOrder> findAllByCartClientEmail(String email);

    List<CustomerOrder> findByRestaurantId(Long restaurantId);

    @Query("SELECT c FROM CustomerOrder c WHERE c.deliveryPerson.email = :email order by c.orderDate asc")
    List<CustomerOrder> findAllByDeliveryPerson(@Param("email") String email);

    @Query("SELECT c FROM CustomerOrder c WHERE c.deliveryPerson.email = :email AND c.currentOrderClientStatus != 'CONCLUIDO'")
    List<CustomerOrder> findActiveOrderDeliveryPerson(String email);
}
