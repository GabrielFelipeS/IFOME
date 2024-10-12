package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "RESTAURANTS")
// TODO esta faltando a parte de user details junto com roles
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
    // TODO arrumar relacionamento
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

    public Restaurant() {}

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

    public Boolean isOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj.replaceAll("[^\\d]", "");
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress( List<Address> address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    public String getPersonResponsible() {
        return personResponsible;
    }

    public void setPersonResponsible(String personResponsible) {
        this.personResponsible = personResponsible;
    }

    public String getPersonResponsibleCpf() {
        return personResponsibleCpf;
    }

    public void setPersonResponsibleCpf(String personResponsibleCPF) {
        this.personResponsibleCpf = personResponsibleCPF.replaceAll("[^\\d]", "");;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean isLoginCorrect(String rawPassword, PasswordEncoder passwordEncoder) {
        System.out.println(rawPassword);
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
