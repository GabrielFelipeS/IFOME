package br.com.ifsp.ifome.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RestaurantDeliveryChat extends Chat {

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryPerson delivery;

    public RestaurantDeliveryChat(CustomerOrder customerOrder) {
        super(customerOrder);
        this.restaurant = customerOrder.getRestaurant();
        this.delivery = customerOrder.getDeliveryPerson();
    }
}
