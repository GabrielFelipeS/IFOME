package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cep;
    private String neighborhood;
    private String city;
    private String state;
    private String address;
    private String number;
    private String complement;
    private String typeResidence;

    public Address(AddressRequest addressRequest) {
        this.cep = addressRequest.cep();
        this.neighborhood = addressRequest.neighborhood();
        this.city = addressRequest.city();
        this.state = addressRequest.state();
        this.address = addressRequest.address();
        this.complement = addressRequest.complement();
        this.number = addressRequest.number();
        this.typeResidence = addressRequest.typeResidence();
    }

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeResidence() {
        return typeResidence;
    }

    public void setTypeResidence(String typeResidence) {
        this.typeResidence = typeResidence;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNeighborhood() {
        return neighborhood;
    }
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getComplement() {
        return complement;
    }
    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

}
