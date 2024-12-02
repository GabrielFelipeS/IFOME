package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
    boolean existsByCustomerOrder_Id(Long customerOrderId);

    @Query("SELECT COALESCE(AVG(r.stars)) FROM RestaurantReview r WHERE r.restaurant.id = :restaurantId")
    double calculateAverageRating(@Param("restaurantId") Long restaurantId);

}
