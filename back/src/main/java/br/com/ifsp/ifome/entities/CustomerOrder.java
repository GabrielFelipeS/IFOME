package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
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
    private Double freight;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderInfo> orderInfo;

    // TODO arrumar um jeito para remover isso
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
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

    public CustomerOrder(Cart cart, LocalDateTime orderDate, Double orderPrice,  Double freight,
                         Restaurant restaurant, DeliveryPerson deliveryPerson, OrderClientStatus status, String paymentStatus)
    {
        this(null, orderPrice, freight, new ArrayList<>(), new ArrayList<>(), paymentStatus, cart, restaurant, deliveryPerson, orderDate, status, null, 0.0);
        this.nextStatus();
    }

    public CustomerOrder(Cart cart, Restaurant restaurant) {
        this(
            null,
            cart.totalPriceWithoutShipping(),
            cart.getFreight(),
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

    public double totalPrice() {
        return this.orderPrice + freight;
    }

    public Client getClient() {return this.cart.getClient();}

    public String getClientEmail() { return this.cart.getEmailClient();}

    public String getClientName() {
        return this.cart.getClientName();
    }

    public String getRestaurantName() {
        return this.restaurant.getNameRestaurant();
    }

    public String getRestaurantEmail() {
        return this.restaurant.getEmail();
    }

    public String getDeliveryEmail() {
        if(this.deliveryPerson == null) {
            return "";
        }

        return this.deliveryPerson.getEmail();
    }

    public Integer getOrderStatusId() {
        return orderInfo.size();
    }

    public Long getDeliveryPersonId() { return deliveryPerson.getId();}

    public String getClientPhone() {
        return this.cart.getClientPhone();
    }

    public Double deliveryCost() {
        return deliveryCost;
    }

    public List<OrderItem> getOrderItems() {
        return this.cart.getOrderItems();
    }

    public Address getClientAddress() {
        return this.cart.getClient().getAddress().get(0);
    }

    public Address getRestaurantAddress() {
        return this.restaurant.getAddress().get(0);
    }

    public String getOrderDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return this.orderDate.format(dateTimeFormatter);
    }

    public String getOrderDateTimeToTimestamp() {
        var orderLastInfo = this.orderInfo.stream()
            .reduce((orderInfoFirst, orderInfoLast) -> orderInfoFirst.getId() > orderInfoLast.getId()
                ? orderInfoFirst : orderInfoLast)
            .orElseThrow();

        return Timestamp.valueOf(orderLastInfo.getLocalDateTime()).toString();
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

        orderInfo.removeIf(orderInfo -> orderInfo.getOrderStatus().equals(currentSize));

        return value;
    }


    public void newDeliveryPersonInfo() {
        this.orderInfoDelivery = List.of(
            new OrderInfoDelivery(
                OrderDeliveryStatus.NOVO,
                LocalDateTime.now(),
                this
            )
         );
    }

    public OrderDeliveryStatus nextDeliveryStatus() {
        int size = orderInfoDelivery.size();

        if(size == OrderDeliveryStatus.values().length) {
            return OrderDeliveryStatus.CONCLUIDO;
        }

        OrderDeliveryStatus value = OrderDeliveryStatus.values()[size];
        System.err.println("Value " + value);

        var newOrderInfo = new OrderInfoDelivery(value, LocalDateTime.now(), this);
        System.err.println(newOrderInfo);
        orderInfoDelivery.add(newOrderInfo);

        System.err.println("Teste 1");
        this.setCurrentOrderDeliveryStatus(value);
        System.err.println("Teste 2");

        return value;
    }

    public OrderInfoDelivery previousStatusDelivery() {
        int size = orderInfoDelivery.size();
        if(size == 0) {
            return new OrderInfoDelivery(OrderDeliveryStatus.NOVO, LocalDateTime.now(), this);
        }

        OrderInfoDelivery value = orderInfoDelivery.get(size - 1);
        value.setCustomerOrder(null);

        orderInfoDelivery.remove(size - 1);

        return value;
    }


    public boolean nextClientStatusByDeliveryStatus() {
        int sizeOrderInfo = this.orderInfo.size();
        int sizeInfoDelivery = this.orderInfoDelivery.size();

        if(sizeOrderInfo == 3 && sizeInfoDelivery == 4) {
           this.nextStatus();
            this.orderInfo.forEach(o -> System.err.println(o.getOrderStatus()));
            return true;
        }

        if(sizeOrderInfo == 4 && sizeInfoDelivery== 5) {
            this.nextStatus();
            this.orderInfo.forEach(o -> System.err.println(o.getOrderStatus()));
            return true;
        }

        return false;
    }

    public boolean getRestaurantEmailDoesNotEquals(String email) {
        return !this.restaurant.getEmail().equals(email);
    }

    public List<OrderInfoDelivery> resetDeliveryPersonInfo() {
        var orderInfoReset = orderInfoDelivery.stream().peek(order -> order.setCustomerOrder(null)).toList();

//        this.orderInfoDelivery = null;

        return orderInfoReset;
    }


}
