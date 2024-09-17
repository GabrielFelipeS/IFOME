package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "RESTAURANTS")
// TODO esta faltando a parte de user details junto com roles
public class Restaurant implements PasswordPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameRestaurant;
    private String cnpj;
    private String foodCategory;
    @OneToMany
    @JoinColumn(name = "address", referencedColumnName = "cnpj")
    private List<Address> address;

    private String telephone;

    private String openingHoursStart;
    private String openingHoursEnd;

    private String personResponsible;
    private String personResponsibleCPF;
    private String email;
    private String password;
    private String paymentMethods;

    private String restaurantImages;

    @Embedded
    private BankAccount bankAccount;

    public Restaurant() {}

    public Restaurant(RestaurantRequest restaurantRequest){
        this.nameRestaurant = restaurantRequest.nameRestaurant();
        this.cnpj = restaurantRequest.cnpj();
        this.foodCategory = restaurantRequest.foodCategory();
        this.address = restaurantRequest.address().stream().map(Address::new).collect(Collectors.toList());
        this.telephone = restaurantRequest.telephone();
        this.openingHoursStart = restaurantRequest.openingHoursStart();
        this.openingHoursEnd = restaurantRequest.openingHoursEnd();
        this.personResponsible = restaurantRequest.personResponsible();
        this.personResponsibleCPF = restaurantRequest.personResponsibleCPF();
        this.email = restaurantRequest.email();
        this.password = restaurantRequest.password();
        this.paymentMethods = restaurantRequest.paymentMethods();
        this.restaurantImages = restaurantRequest.restaurantImages();
        this.bankAccount = new BankAccount(restaurantRequest.bankAccount());
    }

    public Restaurant(Long id, String nameRestaurant, String cnpj, String foodCategory, List<Address> address, String telephone, String openingHoursStart, String openingHoursEnd, String personResponsible, String personResponsibleCPF, String email, String password, String paymentMethods, String restaurantImage, BankAccount bankAccount) {
        this.id = id;
        this.nameRestaurant = nameRestaurant;
        this.cnpj = cnpj;
        this.foodCategory = foodCategory;
        this.address = address;
        this.telephone = telephone;
        this.openingHoursStart = openingHoursStart;
        this.openingHoursEnd = openingHoursEnd;
        this.personResponsible = personResponsible;
        this.personResponsibleCPF = personResponsibleCPF;
        this.email = email;
        this.password = password;
        this.paymentMethods = paymentMethods;
        this.restaurantImages = restaurantImage;
        this.bankAccount = bankAccount;
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
        this.cnpj = cnpj;
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

    public String getOpeningHoursStart() {
        return openingHoursStart;
    }

    public void setOpeningHoursStart(String openingHoursStart) {
        this.openingHoursStart = openingHoursStart;
    }

    public String getOpeningHoursEnd() {
        return openingHoursEnd;
    }

    public void setOpeningHoursEnd(String openingHoursEnd) {
        this.openingHoursEnd = openingHoursEnd;
    }

    public String getPersonResponsible() {
        return personResponsible;
    }

    public void setPersonResponsible(String personResponsible) {
        this.personResponsible = personResponsible;
    }

    public String getPersonResponsibleCPF() {
        return personResponsibleCPF;
    }

    public void setPersonResponsibleCPF(String personResponsibleCPF) {
        this.personResponsibleCPF = personResponsibleCPF;
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

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getRestaurantImages() {
        return restaurantImages;
    }

    public void setRestaurantImages(String restaurantImages) {
        this.restaurantImages = restaurantImages;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public boolean isLoginCorrect(String rawPassword, BCryptPasswordEncoder passwordEncoder) {
        System.out.println(rawPassword);
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
