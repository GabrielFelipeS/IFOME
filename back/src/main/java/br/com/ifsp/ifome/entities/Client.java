package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
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

    @ManyToMany
    @JoinTable(
        name = "tb_users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public Client() {}

    public Client(ClientRequest clientRequest, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.name = clientRequest.name();
        this.email = clientRequest.email();
        this.password = bCryptPasswordEncoder.encode(clientRequest.password());
        this.dateOfBirth = clientRequest.convertDateOfBirth();
        this.cpf = clientRequest.cpf();
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
        this.cpf = cpf;
        this.address = address;
    }

    public Client(Long id, String fullName, String email, String password, LocalDate dateOfBirth, String cpf,  Address address, String paymentMethods, BCryptPasswordEncoder bCryptPasswordEncoder) {
      this(id, fullName, email, password, dateOfBirth, cpf, List.of(address), paymentMethods, bCryptPasswordEncoder);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
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

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress( List<Address> address) {
        this.address = address;
    }

    @Override
    public boolean isLoginCorrect(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
