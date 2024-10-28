package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfo> orderInfo;

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
        this(null, orderPrice,  new ArrayList<>(), paymentStatus, cart, restaurant, deliveryPerson, orderDate);
        this.nextStatus();
    }

    public CustomerOrder(Cart cart, Restaurant restaurant) {
        this(
            null,
            cart.totalPrice(),
            new ArrayList<>(),
            "PENDENTE",
            cart,
            restaurant,
            null,
            LocalDateTime.now());
        cart.setCustomerOrder(this);
        this.nextStatus();
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


    public String getOrderDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
           return this.orderDate.format(dateTimeFormatter);
    }

    public String getOrderDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_TIME;
        return this.orderDate.format(dateTimeFormatter);
    }

    public Address getAddress() {
        return this.cart.getClient().getAddress().get(0);
    }

    public OrderStatus nextStatus() {
        List<OrderStatus> validSequence = List.of(
            OrderStatus.NOVO,
            OrderStatus.EM_PREPARO,
            OrderStatus.PRONTO_PARA_ENTREGA,
            OrderStatus.SAIU_PARA_ENTREGA,
            OrderStatus.CONCLUIDO
        );

        int size = orderInfo.size();
        if(size == OrderStatus.values().length) {
            return OrderStatus.CONCLUIDO;
        }

        System.err.println(OrderStatus.values());
        OrderStatus value = OrderStatus.values()[size];
        var newOrderInfo = new OrderInfo(value, LocalDateTime.now(), this);

        orderInfo.add(newOrderInfo);

        return value;
    }

    public OrderStatus previousStatus() {
        int size = orderInfo.size();
        if(size == 0) {
            return OrderStatus.NOVO;
        }

        OrderStatus currentSize = OrderStatus.values()[size];
        OrderStatus value = OrderStatus.values()[size - 1];

        orderInfo.removeIf(orderInfo -> orderInfo.getOrderStatus().equals(currentSize));

        return value;
    }

    public Integer getOrderStatusId() {
        return orderInfo.size();
    }


}
