package br.com.ifsp.ifome.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefuseCustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerOrderId;
    private Long deliveryId;
    private String justification;

    public RefuseCustomerOrder(Long customerOrderId, Long deliveryId, String justification) {
        this.customerOrderId = customerOrderId;
        this.deliveryId = deliveryId;
        this.justification = justification;
    }
}
