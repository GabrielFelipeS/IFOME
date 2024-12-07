package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>
{
    List<CustomerOrder> findAllByCartClientEmailAndCurrentOrderClientStatus(String email, OrderClientStatus orderClientStatus);

    @Query("SELECT co FROM CustomerOrder co WHERE co.currentOrderClientStatus != 'CONCLUIDO' AND co.cart.client.email = :email")
    List<CustomerOrder> findAllByCartClientEmail(@Param("email") String email);

    @Query("SELECT c FROM CustomerOrder c WHERE c.restaurant.id = :id ORDER BY c.orderDate desc")
    List<CustomerOrder> findByRestaurantId(@Param("id") Long restaurantId);

    @Query("SELECT c FROM CustomerOrder c WHERE c.deliveryPerson.email = :email order by c.orderDate desc")
    List<CustomerOrder> findAllByDeliveryPerson(@Param("email") String email);

    @Query("SELECT c FROM CustomerOrder c WHERE c.deliveryPerson.email = :email AND c.currentOrderClientStatus != 'CONCLUIDO'")
    List<CustomerOrder> findActiveOrderDeliveryPerson(String email);
}
