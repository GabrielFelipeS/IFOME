package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.ClientLoginRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
public class Client implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String cpf;

    @OneToMany
    @JoinColumn(name = "address", referencedColumnName = "cpf")
    private List<Address> address;

    @ManyToMany
    @JoinTable(
        name = "tb_users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public Client() {}

    public Client(ClientRequest clientRequest, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.email = clientRequest.email();
        this.password = bCryptPasswordEncoder.encode(clientRequest.password());
        this.dateOfBirth = clientRequest.dateOfBirth();
        this.cpf = clientRequest.cpf();
        this.address = clientRequest.address().stream().map(Address::new).collect(Collectors.toList());
    }

    public Client(Long id, String email, String password, LocalDate dateOfBirth, String cpf,  List<Address> address, String paymentMethods) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.cpf = cpf;
        this.address = address;
    }

    public Client(Long id, String email, String password, LocalDate dateOfBirth, String cpf,  Address address, String paymentMethods) {
      this(id, email, password, dateOfBirth, cpf, List.of(address), paymentMethods);
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

    public boolean isLoginIncorrect(ClientLoginRequest clientLoginRequest, BCryptPasswordEncoder passwordEncoder) {
        return !passwordEncoder.matches(clientLoginRequest.password(), this.password);
    }
}
