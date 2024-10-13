package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_address", length = 100, nullable = false)
    private String nameAddress;

    @Column(length = 10, nullable = false)
    private String cep;

    @Column(length = 150, nullable = false)
    private String neighborhood;

    @Column(length = 150, nullable = false)
    private String city;

    @Column(length = 150, nullable = false)
    private String state;

    @Column(length = 150, nullable = false)
    private String address;

    @Column(length = 150, nullable = false)
    private String number;

    @Column(nullable = false)
    private String complement;

    private String details;

    @Column(length = 150, nullable = false)
    private String typeResidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryPerson delivery;

    public Address(AddressRequest addressRequest) {
        this.cep = addressRequest.cep();
        this.nameAddress = addressRequest.nameAddress();
        this.neighborhood = addressRequest.neighborhood();
        this.city = addressRequest.city();
        this.state = addressRequest.state();
        this.address = addressRequest.address();
        this.complement = addressRequest.complement();
        this.number = addressRequest.number();
        this.typeResidence = addressRequest.typeResidence();
        this.details = addressRequest.details();
    }
}
