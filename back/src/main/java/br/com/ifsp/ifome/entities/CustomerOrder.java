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
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Double orderPrice;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryPerson deliveryPerson;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NOVO; // Estado inicial é NOVO

    private String paymentStatus;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @CreationTimestamp
    private LocalDateTime orderDate;

    public CustomerOrder(List<OrderItem> orderItems, LocalDateTime orderDate, Double orderPrice, Restaurant restaurant, DeliveryPerson deliveryPerson, OrderStatus status, String paymentStatus)
    {
        this(
            null,
            orderPrice,
            restaurant,
            deliveryPerson,
            status,
            paymentStatus,
            orderItems,
            orderDate);
    }

    public void calculateTotalPrice() {
        this.orderPrice = orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

}
