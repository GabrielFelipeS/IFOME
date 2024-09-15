package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "delivery_person")
public class DeliveryPerson  implements PasswordPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cpf;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String typeOfVehicle;
    private String telephone;
    private String cnh;
    private String vehicleDocument;
    @OneToMany
    @JoinColumn(name = "address", referencedColumnName = "cpf")
    private List<Address> address;
    @Embedded
    private BankAccount bankAccount;

    public DeliveryPerson(){}

    public DeliveryPerson(DeliveryPersonRequest deliveryPersonRequest) {
        this.name = deliveryPersonRequest.name();
        this.cpf = deliveryPersonRequest.cpf();
        this.email = deliveryPersonRequest.email();
        this.password = deliveryPersonRequest.password();
        this.dateOfBirth =deliveryPersonRequest.dateOfBirth();
        this.typeOfVehicle = deliveryPersonRequest.vehicleDocument();
        this.telephone = deliveryPersonRequest.telephone();
        this.cnh = deliveryPersonRequest.cnh();
        this.vehicleDocument = deliveryPersonRequest.vehicleDocument();
        this.address = deliveryPersonRequest.address().stream().map(Address::new).collect(Collectors.toList());
        this.bankAccount = new BankAccount(deliveryPersonRequest.bankAccount());
    }


    public DeliveryPerson(Long id, String name, String cpf, String email, String password, LocalDate dateOfBirth, String typeOfVehicle, String telephone, String cnh, String vehicleDocument, List<Address> address, BankAccount bankAccount) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.typeOfVehicle = typeOfVehicle;
        this.telephone = telephone;
        this.cnh = cnh;
        this.vehicleDocument = vehicleDocument;
        this.address = address;
        this.bankAccount = bankAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(String typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getVehicleDocument() {
        return vehicleDocument;
    }

    public void setVehicleDocument(String vehicleDocument) {
        this.vehicleDocument = vehicleDocument;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
