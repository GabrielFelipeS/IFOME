package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double orderPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NOVO;

    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryPerson deliveryPerson;

    @CreationTimestamp
    private LocalDateTime orderDate;

    public CustomerOrder(Cart cart, LocalDateTime orderDate, Double orderPrice,
                         Restaurant restaurant, DeliveryPerson deliveryPerson, OrderStatus status, String paymentStatus)
    {
        this(null, orderPrice, OrderStatus.NOVO, paymentStatus, cart, restaurant, deliveryPerson, orderDate);
    }

    public CustomerOrder(Cart cart, Restaurant restaurant) {
        this(
            null,
            cart.totalPrice(),
            OrderStatus.NOVO,
            "PENDENTE",
            cart,
            restaurant,
            null,
            LocalDateTime.now());
        cart.setCustomerOrder(this);
    }

    public void calculateTotalPrice() {
        this.orderPrice = cart.getOrderItems().stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public String getEmailClient() {
        return this.cart.getEmailClient();
    }

    public String getClientName() {
        return this.cart.getClientName();
    }

    public String getRestaurantName() {
        return this.restaurant.getNameRestaurant();
    }

    public String getStatusMessage() {
        return status.toString()
                .toLowerCase(Locale.ROOT)
                .replaceAll("_", " ");
    }

}
