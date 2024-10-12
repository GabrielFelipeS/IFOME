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
    private String nameAddress;
    private String cep;
    private String neighborhood;
    private String city;
    private String state;
    private String address;
    private String number;
    private String complement;
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
    }
}
