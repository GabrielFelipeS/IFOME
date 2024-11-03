package br.com.ifsp.ifome.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderDeliveryStatus orderDeliveryStatus;
    private LocalDateTime localDateTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_order")
    private CustomerOrder customerOrder;

    public OrderInfoDelivery(OrderDeliveryStatus orderDeliveryStatus, LocalDateTime localDateTime, CustomerOrder customerOrder) {
        this.orderDeliveryStatus = orderDeliveryStatus;
        this.localDateTime = localDateTime;
        this.customerOrder = customerOrder;
        System.err.println("TESTE");
    }
}
