package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "delivery_person")
public class DeliveryPerson  implements PasswordPolicy, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "type_of_vehicle", nullable = false)
    private String typeOfVehicle;

    @Column(name = "plate", nullable = false)
    private String plate;

    @Column(length = 15, nullable = false)
    private String telephone;

    @Column(name = "cnh_number", length = 11, nullable = false, unique = true)
    private String cnhNumber;

    @Column(name = "cnh_validity", nullable = false)
    private LocalDate cnhValidity;

    @Column(name = "vehicle_document", length = 15, nullable = false)
    private String vehicleDocument;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> address;

    @Embedded
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    private Role role;

    public DeliveryPerson(DeliveryPersonRequest deliveryPersonRequest, PasswordEncoder passwordEncoder) {
        this.name = deliveryPersonRequest.name();
        this.cpf = deliveryPersonRequest.cpf().replaceAll("[^\\d]", "");
        this.email = deliveryPersonRequest.email();
        this.password = passwordEncoder.encode(deliveryPersonRequest.password());
        this.dateOfBirth =deliveryPersonRequest.convertDateOfBirth();
        this.typeOfVehicle = deliveryPersonRequest.vehicleDocument();
        this.plate = deliveryPersonRequest.plate();
        this.telephone = deliveryPersonRequest.telephone();
        this.cnhNumber = deliveryPersonRequest.cnhNumber();
        this.cnhValidity = deliveryPersonRequest.cnhValidity();
        this.vehicleDocument = deliveryPersonRequest.vehicleDocument();
        this.bankAccount = new BankAccount(deliveryPersonRequest.bankAccount());
        this.role = Role.DELIVERY;
        this.setAddress(deliveryPersonRequest.address());
    }


    public DeliveryPerson(Long id, String name, String cpf, String email, String password,
                          LocalDate dateOfBirth, String typeOfVehicle, String telephone,
                          String cnh, String vehicleDocument, List<Address> address,
                          BankAccount bankAccount,  PasswordEncoder passwordEncoder) {}

    public DeliveryPerson(Long id, String name, String cpf, String email, String password, LocalDate dateOfBirth, String typeOfVehicle, String plate,String telephone, String cnhNumber, LocalDate cnhValidity, String vehicleDocument, List<Address> address, BankAccount bankAccount, PasswordEncoder passwordEncoder) {
        this.id = id;
        this.name = name;
        this.cpf = cpf.replaceAll("[^\\d]", "");
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

    public void setAddress(AddressRequest addressRequest) {
        Address address = new Address(addressRequest);
        address.setDelivery(this);
        this.address = List.of(address);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public boolean isLoginCorrect(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
