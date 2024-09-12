package br.com.ifsp.ifome.ifome.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String cpf;
    private String typeResidence;
    private String cep;
    private String address;
    private String paymentMethods;

    public Client() {}

    public Client(Long id, String email, String password, LocalDate dateOfBirth, String cpf, String typeResidence, String cep, String address, String paymentMethods) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.cpf = cpf;
        this.typeResidence = typeResidence;
        this.cep = cep;
        this.address = address;
        this.paymentMethods = paymentMethods;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
