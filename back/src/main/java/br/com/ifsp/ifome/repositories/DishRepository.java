package br.com.ifsp.ifome.repositories;

import br.com.ifsp.ifome.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%' AND d.restaurant.id = :id")
    List<Dish> findAllByRestaurantId(@Param("id") Long id);

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%'")
    List<Dish> findAllAvailable(Sort sort);

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%'")
    Page<Dish> findAllAvailable(Pageable name);

    @Query("SELECT d FROM Dish d WHERE d.availability LIKE '%Disponível%' AND d.id = :id")
    Optional<Dish> findDishAvailableById(Long id);

    List<Dish> findByNameContainingIgnoreCase(String query);

    List<Dish> findByDishCategory(String category);
}
