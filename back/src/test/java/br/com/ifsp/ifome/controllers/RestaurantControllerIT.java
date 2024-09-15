package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.BankAccountRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.BankAccount;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DirtiesContext
    @DisplayName("Should be possible to create a new Restaurant")
    public void  shouldBeAbleToCreateANewRestaurant(){
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.id");
        String nameRestaurant = document.read("$.nameRestaurant");
        String email = document.read("$.email");
        String cnpj = document.read("$.cnpj");
        //String address = document.read("$.address");
        Address addressJson = document.read("$.address[0]", Address.class);
        String telephone = document.read("$.telephone");
        String cep = document.read("$.cep");
        String foodCategory = document.read("$.foodCategory");
        String paymentMethods = document.read("$.paymentMethods");
        String openingHoursStart = document.read("$.openingHoursStart");
        String openingHoursEnd = document.read("$.openingHoursEnd");
        String personResponsibleCPF = document.read("$.personResponsibleCPF");
        String restaurantImages = document.read("$.restaurantImage");


        assertThat(id).isNotNull();
        assertThat(email).isEqualTo(restaurant.email());
        assertThat(cnpj).isEqualTo(restaurant.cnpj());

        assertThat(addressJson).isNotNull();
        assertThat(addressJson).isNotNull();

        assertThat(addressJson.getCep()).isEqualTo("35170-222");
        assertThat(addressJson.getNeighborhood()).isEqualTo("neighborhood");
        assertThat(addressJson.getCity()).isEqualTo("city");
        assertThat(addressJson.getAddress()).isEqualTo("address");
        assertThat(addressJson.getComplement()).isEqualTo("complement");
        assertThat(addressJson.getNumber()).isEqualTo("12");
        assertThat(addressJson.getComplement()).isEqualTo("complement");

    }

    @Test
    @DirtiesContext
    @DisplayName("should not be possible to create a new restaurant with already registered email")
    public void shouldReturnErrorWhenCreatingRestaurantWithAlreadyRegisteredEmail() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email1@email.com",
                "@Senha1",
                "@Senha1",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest("123","1255", "4547-7")


        );

        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.email");

        assertThat(message).containsExactlyInAnyOrder("E-mail já registrado");
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the password fields")
    public void shouldReturnAllValidationErrorsInThePasswordFields() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                " ",
                " ",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest("123","1255", "4547-7")


        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(2);

        List<String> passwordErrors = documentContext.read("$.password");
        assertThat(passwordErrors)
                .containsExactlyInAnyOrder(
                        "Senha é obrigatório",
                        "A senha precisa possui pelo menos 6 caracteres",
                        "Senha precisa conter pelo menos um número",
                        "Senha precisa conter pelo menos um caractere minúsculo",
                        "Senha precisa conter pelo menos um caractere maiúsculo",
                        "Senha precisa conter pelo menos um caractere especial",
                        "Senha não pode conter espaço");
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the cnpj field")
    public void shouldReturnAllValidationErrorsInTheCNPJField() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                "10.882.594000165",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest("123","1255", "4547-7")


        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> cnpj = documentContext.read("$.cnpj");
        assertThat(cnpj)
                .containsExactlyInAnyOrder(
                        "CNPJ inválido"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the CPF field")
    public void shouldReturnAllValidationErrorsInThePersonResponsibleCPFField() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "CPF",
                "imagem.jpeg",
                new BankAccountRequest("123","1255", "4547-7")


        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> personResponsibleCPF = documentContext.read("$.personResponsibleCPF");
        assertThat(personResponsibleCPF)
                .containsExactlyInAnyOrder(
                        "CPF inválido"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the password confirmation field")
    public void shouldReturnAllValidationErrorsInThePasswordConfirmationField() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                "@Senha1",
                "@senha",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest("123","1255", "4547-7")


        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the bank account fields")
    public void shouldReturnAllValidationErrorsInTheBankAccountFields() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest(" "," ", "")


        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(3);
    }
    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the address fields")
    public void shouldReturnAllValidationErrorsInTheAddressFields() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "Nome Restaurante",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                "10.882.594/0001-65",
                List.of(new AddressRequest("35170-222", "casa 1"," ", "city", "state",
                        "address", "complement",
                        "12", "details")),
                "(11) 1234-5678",
                "07070-000",
                "Pizzaria",
                "Dinheiro, Cartão",
                "12:00",
                "23:00",
                "responsavel",
                "033.197.356-16",
                "imagem.jpeg",
                new BankAccountRequest("111","2222", "2156-1")


        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/restaurant", restaurant, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);
    }
}




