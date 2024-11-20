package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.RefuseCustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefuseCustomerOrderRepository extends JpaRepository<RefuseCustomerOrder, Long> {
    List<RefuseCustomerOrder> findRefuseCustomerOrderByCustomerOrderId(Long id);
}
