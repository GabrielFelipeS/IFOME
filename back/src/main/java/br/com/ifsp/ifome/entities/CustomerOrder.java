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

    // TODO arrumar um jeito para remover isso
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfoDelivery> orderInfoDelivery;

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
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderClientStatus currentOrderClientStatus;

    @Enumerated(EnumType.STRING)
    private OrderDeliveryStatus currentOrderDeliveryStatus;

    private Double deliveryCost;

    public CustomerOrder(Cart cart, LocalDateTime orderDate, Double orderPrice,
                         Restaurant restaurant, DeliveryPerson deliveryPerson, OrderClientStatus status, String paymentStatus)
    {
        this(null, orderPrice,  new ArrayList<>(), new ArrayList<>(), paymentStatus, cart, restaurant, deliveryPerson, orderDate, status, null, 0.0);
        this.nextStatus();
    }

    public CustomerOrder(Cart cart, Restaurant restaurant) {
        this(
            null,
            cart.totalPrice(),
            new ArrayList<>(),
            new ArrayList<>(),
            "PENDENTE",
            cart,
            restaurant,
            null,
            LocalDateTime.now(),
            OrderClientStatus.NOVO,
            null,
            0.0);
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

    public List<OrderItem> getOrderItems() {
        return this.cart.getOrderItems();
    }

    public String getOrderDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
           return this.orderDate.format(dateTimeFormatter);
    }

    public String getOrderDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_TIME;
        return this.orderDate.format(dateTimeFormatter);
    }

    public Address getClientAddress() {
        return this.cart.getClient().getAddress().get(0);
    }

    public Address getRestaurantAddress() {
        return this.restaurant.getAddress().get(0);
    }

    public OrderClientStatus nextStatus() {
        int size = orderInfo.size();
        if(size == OrderClientStatus.values().length) {
            return OrderClientStatus.CONCLUIDO;
        }

        System.err.println(OrderClientStatus.values());
        OrderClientStatus value = OrderClientStatus.values()[size];
        var newOrderInfo = new OrderInfo(value, LocalDateTime.now(), this);

        orderInfo.add(newOrderInfo);
        this.setCurrentOrderClientStatus(value);

        return value;
    }

    public OrderClientStatus previousStatus() {
        int size = orderInfo.size();
        if(size == 0) {
            return OrderClientStatus.NOVO;
        }

        OrderClientStatus currentSize = OrderClientStatus.values()[size];
        OrderClientStatus value = OrderClientStatus.values()[size - 1];

        orderInfo.removeIf(orderInfo -> orderInfo.getOrderClientStatus().equals(currentSize));

        return value;
    }

    public Integer getOrderStatusId() {
        return orderInfo.size();
    }



    public String getClientPhone() {
        return this.cart.getClientPhone();
    }


    public Double deliveryCost() {
        return deliveryCost;
    }

    public OrderDeliveryStatus nextDeliveryStatus() {
        int size = orderInfo.size();
        if(size == OrderClientStatus.values().length) {
            return OrderDeliveryStatus.CONCLUIDO;
        }

        OrderDeliveryStatus value = OrderDeliveryStatus.values()[size];
        var newOrderInfo = new OrderInfoDelivery(value, LocalDateTime.now(), this);

        orderInfoDelivery.add(newOrderInfo);
        this.setCurrentOrderDeliveryStatus(value);

        return value;
    }

    public OrderDeliveryStatus previousStatusDelivery() {
        int size = orderInfo.size();
        if(size == 0) {
            return OrderDeliveryStatus.NOVO;
        }

        OrderDeliveryStatus currentSize = OrderDeliveryStatus.values()[size];
        OrderDeliveryStatus value = OrderDeliveryStatus.values()[size - 1];

        orderInfoDelivery.removeIf(orderInfoDelivery -> orderInfoDelivery.getOrderDeliveryStatus().equals(currentSize));

        return value;
    }
}
