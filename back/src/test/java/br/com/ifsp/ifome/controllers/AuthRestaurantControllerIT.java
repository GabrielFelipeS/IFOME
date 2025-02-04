package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.*;
import br.com.ifsp.ifome.entities.Address;
import br.com.ifsp.ifome.entities.OpeningHours;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthRestaurantControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DirtiesContext
    public void shouldBeAbleLoginWithValidUser() {
        LoginRequest restaurantLogin = new LoginRequest("email1@email.com", "@Password1");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant/login", restaurantLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Object clientResponse = documentContext.read("$.data.restaurant");
        assertThat(clientResponse).isNotNull();

        String token = documentContext.read("$.data.token");
        assertThat(token).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " +token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseTokenValidation = testRestTemplate.exchange("/api/auth/token", HttpMethod.POST, request, String.class);
        assertThat(responseTokenValidation.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldReturnErrorWhenLoginWithInvalidEmail() {
        LoginRequest restaurantLogin = new LoginRequest("invalid_email@gmail.com", "@Password1");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant/login", restaurantLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldReturnErrorWhenLoginWithInvalidPassword() {
        LoginRequest restaurantLogin = new LoginRequest("user1@gmail.com", "invalid_password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant/login", restaurantLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    @DirtiesContext
    @DisplayName("Should be possible to create a new Restaurant")
    public void  shouldBeAbleToCreateANewRestaurant(){
        RestaurantRequest restaurant = new RestaurantRequest(
            "Nome Restaurante",
            "email@email.com",
            "@Senha1",
            "@Senha1",
            "10882594000165",
            new AddressRequest("35170-222", "casa 1","Vila Rio", "Guarulhos", "São Paulo",
                "Av. Salgado Filho", "Em frente a pizzaria",
                "3501", "casa","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")

        );

        // Carregar o arquivo de exemplo do classpath
        ClassPathResource fileResource = new ClassPathResource("image.png");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        // Definir os headers da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity(
            "/api/auth/restaurant",
            requestEntity,
            String.class);

        System.out.println(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.data.id");
        String nameRestaurant = document.read("$.data.nameRestaurant");
        String email = document.read("$.data.email");
        String cnpj = document.read("$.data.cnpj");
        //String address = document.read("$.data.address");
        Address addressJson = document.read("$.data.address[0]", Address.class);
        String telephone = document.read("$.data.telephone");
        String foodCategory = document.read("$.data.foodCategory");
        String paymentMethods = document.read("$.data.paymentMethods");
        OpeningHours openingHoursJson = document.read("$.data.openingHours", OpeningHours.class);
        String personResponsibleCPF = document.read("$.data.personResponsibleCPF");
        String restaurantImages = document.read("$.data.restaurantImage");
        Boolean isOpen = document.read("$.data.isOpen");


        assertThat(id).isNotNull();
        assertThat(email).isEqualTo(restaurant.email());
        assertThat(cnpj).isEqualTo(restaurant.cnpj());
        assertThat(isOpen).isEqualTo(false);

        assertThat(addressJson).isNotNull();
        assertThat(addressJson).isNotNull();

        assertThat(addressJson.getCep()).isEqualTo("35170-222");
        assertThat(addressJson.getNeighborhood()).isEqualTo("Vila Rio");
        assertThat(addressJson.getCity()).isEqualTo("Guarulhos");
        assertThat(addressJson.getAddress()).isEqualTo("Av. Salgado Filho");
        assertThat(addressJson.getComplement()).isEqualTo("Em frente a pizzaria");
        assertThat(addressJson.getNumber()).isEqualTo("3501");
        assertThat(addressJson.getTypeResidence()).isEqualTo("casa");
    }

    @Test
    @DirtiesContext
    @DisplayName("should be return error with cnpj already registred")
    public void shouldReturnErrorWithCnpjAlreadyRegistred() throws JsonProcessingException {
        RestaurantRequest restaurant = new RestaurantRequest(
            "Nome Restaurante",
            "email@email.com",
            "@Senha1",
            "@Senha1",
            "58.911.612/0001-16",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
                "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")


        );

        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
            "/api/auth/restaurant",
            body,
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.errors.cnpj");

        assertThat(message).containsExactlyInAnyOrder("Cnpj já cadastrado");
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
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.errors.email");

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
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")


        );

        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(2);

        List<String> passwordErrors = documentContext.read("$.errors.password");
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
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> cnpj = documentContext.read("$.errors.cnpj");
        assertThat(cnpj)
            .containsExactlyInAnyOrder(
                "O campo \"CNPJ\" deve estar no formato XX.XXX.XXX/XXXX-XX"
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
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "CPF",
            new BankAccountRequest("123","1255", "4547-7")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> personResponsibleCPF = documentContext.read("$.errors.personResponsibleCPF");
        assertThat(personResponsibleCPF)
            .containsExactlyInAnyOrder(
                "O campo \"CPF\" deve estar no formato: XXX.XXX.XXX-XX"
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
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
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
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest(" "," ", "")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");
        System.out.println("File?");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
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
            new AddressRequest("35170-222", "casa 1"," ", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("111","2222", "2156-1")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    @DisplayName("should return errors in the OpeningHours field")
    public void shouldReturnErrorsInOpeningHoursField() {
        RestaurantRequest restaurant = new RestaurantRequest(
            "Nome Restaurante",
            "email@email.com",
            "@Senha1",
            "@Senha1",
            "10.882.594/0001-65",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segun","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")


        );
        ClassPathResource fileResource = new ClassPathResource("image.png");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/restaurant", body, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);
    }
}




