package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RESTAURANTS")
public class Restaurant implements PasswordPolicy, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameRestaurant;
    private String cnpj;
    private String foodCategory;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> address;

    private String telephone;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OpeningHours> openingHours;
    private String personResponsible;
    private String personResponsibleCpf;
    private String email;
    private String password;
    private String paymentMethods;
    private String restaurantImage;
    @Embedded
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Dish> dishes;

    private Boolean isOpen;

    public Restaurant(RestaurantRequest restaurantRequest, BCryptPasswordEncoder bCryptPasswordEncoder, String restaurantImage){
        this.nameRestaurant = restaurantRequest.nameRestaurant();
        this.cnpj = restaurantRequest.cnpj();
        this.foodCategory = restaurantRequest.foodCategory();
        this.address = restaurantRequest.address().stream().map(addressRequest -> {
            Address address = new Address(addressRequest);
            address.setRestaurant(this);
            return address;
        }).collect(Collectors.toList());
        this.telephone = restaurantRequest.telephone();
        this.openingHours = restaurantRequest.openingHours().stream().map(openingHoursRequest -> {
            OpeningHours openingHours1 = new OpeningHours(openingHoursRequest);
            openingHours1.setRestaurant(this);
            return openingHours1;
        }).collect(Collectors.toList());
        this.personResponsible = restaurantRequest.personResponsible();
        this.personResponsibleCpf = restaurantRequest.personResponsibleCPF().replaceAll("[^\\d]", "");
        this.email = restaurantRequest.email();
        this.password = bCryptPasswordEncoder.encode(restaurantRequest.password());
        this.paymentMethods = restaurantRequest.paymentMethods();
        this.restaurantImage = restaurantImage;
        this.bankAccount = new BankAccount(restaurantRequest.bankAccount());
        this.isOpen = false;
    }

    public Restaurant(Long id, String nameRestaurant, String cnpj,
                      String foodCategory, List<Address> address, String telephone,
                      List<OpeningHours> openingHours, String personResponsible,
                      String personResponsibleCPF, String email, String password, String paymentMethods,
                      String restaurantImage, BankAccount bankAccount, boolean isOpen,  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.id = id;
        this.nameRestaurant = nameRestaurant;
        this.cnpj = cnpj.replaceAll("[^\\d]", "");
        this.foodCategory = foodCategory;
        this.address = address;
        this.telephone = telephone;
        this.openingHours = openingHours;
        this.personResponsible = personResponsible;
        this.personResponsibleCpf = personResponsibleCPF.replaceAll("[^\\d]", "");
        this.email = email;
        this.password = bCryptPasswordEncoder.encode(password);
        this.paymentMethods = paymentMethods;
        this.restaurantImage = restaurantImage;
        this.bankAccount = bankAccount;
        this.role = Role.RESTAURANT;
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return this.getIsOpen();
    }

    public void reverseStatusOpen() {
        this.setIsOpen(!this.isOpen());
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
        System.out.println(rawPassword);
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
