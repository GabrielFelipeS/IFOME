package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.ClientRequest;
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
    @Embedded
    private Address address;
    private String paymentMethods;

    public Client() {}

    public Client(ClientRequest clientRequest) {
        this.email = clientRequest.email();
        this.password = clientRequest.password();
        this.dateOfBirth = clientRequest.dateOfBirth();
        this.cpf = clientRequest.cpf();
        this.address = new Address(clientRequest.address());
        this.paymentMethods = clientRequest.paymentMethods();
    }

    public Client(Long id, String email, String password, LocalDate dateOfBirth, String cpf, Address address, String paymentMethods) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.cpf = cpf;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
