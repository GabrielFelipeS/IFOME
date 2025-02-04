package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client implements PasswordPolicy, UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private LocalDate dateOfBirth;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 15, nullable = false)
    private String phone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Address> address= new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public Client(ClientRequest clientRequest, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.name = clientRequest.name();
        this.email = clientRequest.email();
        this.password = bCryptPasswordEncoder.encode(clientRequest.password());
        this.dateOfBirth = clientRequest.convertDateOfBirth();
        this.cpf = clientRequest.cpf().replaceAll("[^\\d]", "");
        this.phone = clientRequest.phone();
        this.role = Role.CLIENT;
        this.setAddress(clientRequest.address());
    }

    public Client(Long id, String name, String email, String password, LocalDate dateOfBirth, String cpf,  List<Address> address, String paymentMethods, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = bCryptPasswordEncoder.encode(password);
        this.dateOfBirth = dateOfBirth;
        this.cpf = cpf.replaceAll("[^\\d]", "");
        this.address = address;
        this.role = Role.CLIENT;
    }

    public Client(Long id, String fullName, String email, String password, LocalDate dateOfBirth, String cpf,  Address address, String paymentMethods, BCryptPasswordEncoder bCryptPasswordEncoder) {
      this(id, fullName, email, password, dateOfBirth, cpf, List.of(address), paymentMethods, bCryptPasswordEncoder);
    }

    public void setAddress(AddressRequest addressRequest) {
        Address address = new Address(addressRequest);
        address.setClient(this);
        this.address = List.of(address);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
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
