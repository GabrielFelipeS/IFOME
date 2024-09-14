package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.AddressRequest;

public class Address {
    String cep;
    String neighborhood;
    String city;
    String state;
    String address;
    String zipCode;
    String typeResidence;
    String number;
    String complement;
    String walk;

    public Address(AddressRequest addressRequest) {
        this.cep = addressRequest.cep();
        this.neighborhood = addressRequest.neighborhood();
        this.city = addressRequest.city();
        this.state = addressRequest.state();
        this.address = addressRequest.address();
        this.zipCode = addressRequest.zipCode();
        this.complement = addressRequest.complement();
        this.typeResidence = addressRequest.typeResidence();
        this.number = addressRequest.number();
        this.walk = addressRequest.walk();
    }

    public Address() {
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

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getComplement() {
        return complement;
    }
    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getTypeResidence() {
        return typeResidence;
    }
    public void setTypeResidence(String typeResidence) {
        this.typeResidence = typeResidence;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getWalk() {
        return walk;
    }
    public void setWalk(String walk) {
        this.walk = walk;
    }
}
