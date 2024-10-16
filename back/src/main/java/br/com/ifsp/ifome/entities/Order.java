package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @OneToMany(mappedBy = "order_id", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @CreationTimestamp
    private LocalDateTime orderDate;
    private Double orderPrice;
    private Restaurant restaurant;
    private DeliveryPerson deliveryPerson;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NOVO; // Estado inicial é NOVO

    private String paymentStatus;

    public Order(List<OrderItem> orderItems, LocalDateTime orderDate, Double orderPrice, Restaurant restaurant, DeliveryPerson deliveryPerson, OrderStatus status, String paymentStatus)
    {
        this(null,
                 orderItems,
                 orderDate,
                 orderPrice,
                 restaurant,
                 deliveryPerson,
                 status,
                paymentStatus);
    }

    public void calculateTotalPrice() {
        this.orderPrice = orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

}
