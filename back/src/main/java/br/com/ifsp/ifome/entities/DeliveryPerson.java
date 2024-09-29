package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "delivery_person")
// TODO esta faltando a parte de user details junto com roles
public class DeliveryPerson  implements PasswordPolicy, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cpf;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String typeOfVehicle;
    private String plate;
    private String telephone;
    private String cnhNumber;
    private LocalDate cnhValidity;
    private String vehicleDocument;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> address;
    @Embedded
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    private Role role;

    public DeliveryPerson(){}

    public DeliveryPerson(DeliveryPersonRequest deliveryPersonRequest, PasswordEncoder passwordEncoder) {
        this.name = deliveryPersonRequest.name();
        this.cpf = deliveryPersonRequest.cpf();
        this.email = deliveryPersonRequest.email();
        this.password = passwordEncoder.encode(deliveryPersonRequest.password());
        this.dateOfBirth =deliveryPersonRequest.convertDateOfBirth();
        this.typeOfVehicle = deliveryPersonRequest.vehicleDocument();
        this.plate = deliveryPersonRequest.plate();
        this.telephone = deliveryPersonRequest.telephone();
        this.cnhNumber = deliveryPersonRequest.cnhNumber();
        this.cnhValidity = deliveryPersonRequest.cnhValidity();
        this.vehicleDocument = deliveryPersonRequest.vehicleDocument();
        this.address = deliveryPersonRequest.address().stream().map(addressRequest -> {
            Address address = new Address(addressRequest);
            address.setDelivery(this);
            return address;
        }).collect(Collectors.toList());
        this.bankAccount = new BankAccount(deliveryPersonRequest.bankAccount());

    }


    public DeliveryPerson(Long id, String name, String cpf, String email, String password,
                          LocalDate dateOfBirth, String typeOfVehicle, String telephone,
                          String cnh, String vehicleDocument, List<Address> address,
                          BankAccount bankAccount,  PasswordEncoder passwordEncoder) {}

    public DeliveryPerson(Long id, String name, String cpf, String email, String password, LocalDate dateOfBirth, String typeOfVehicle, String plate,String telephone, String cnhNumber, LocalDate cnhValidity, String vehicleDocument, List<Address> address, BankAccount bankAccount, PasswordEncoder passwordEncoder) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.dateOfBirth = dateOfBirth;
        this.typeOfVehicle = typeOfVehicle;
        this.plate = plate;
        this.telephone = telephone;
        this.cnhNumber = cnhNumber;
        this.cnhValidity = cnhValidity;
        this.vehicleDocument = vehicleDocument;
        this.address = address;
        this.bankAccount = bankAccount;
        this.role = Role.DELIVERY;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
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

    public String getCnhNumber() {
        return cnhNumber;
    }

    public void setCnhNumber(String cnhNumber) {
        this.cnhNumber = cnhNumber;
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public LocalDate getCnhValidity() {
        return cnhValidity;
    }

    public void LocalDate(LocalDate cnhValidity) {
        this.cnhValidity = cnhValidity;
    }

    @Override
    public boolean isLoginCorrect(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
