package br.com.ifsp.ifome.entities;

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
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client implements PasswordPolicy, UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String cpf;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> address= new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public Client(ClientRequest clientRequest, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.name = clientRequest.name();
        this.email = clientRequest.email();
        this.password = bCryptPasswordEncoder.encode(clientRequest.password());
        this.dateOfBirth = clientRequest.convertDateOfBirth();
        this.cpf = clientRequest.cpf().replaceAll("[^\\d]", "");
        this.address = clientRequest.address().stream().map(addressRequest -> {
            Address address = new Address(addressRequest);
            address.setClient(this);
            return address;
        }).collect(Collectors.toList());

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
