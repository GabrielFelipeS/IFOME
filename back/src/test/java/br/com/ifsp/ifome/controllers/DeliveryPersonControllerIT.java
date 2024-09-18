package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.BankAccountRequest;
import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeliveryPersonControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DirtiesContext
    public void shouldBeAbleLoginWithValidUser() {
        LoginRequest clientLogin = new LoginRequest("email1@email.com", "@Password1");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Object clientResponse = documentContext.read("$.data.user");
        assertThat(clientResponse).isNotNull();

        String token = documentContext.read("$.data.token");
        assertThat(token).isNotNull();

        ResponseEntity<String> responseTokenValidation = testRestTemplate.postForEntity("/api/auth/token", token, String.class);
        assertThat(responseTokenValidation.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldReturnErrorWhenLoginWithInvalidEmail() {
        LoginRequest clientLogin = new LoginRequest("invalid_email@gmail.com", "@Password1");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnErrorWhenLoginWithInvalidPassword() {
        LoginRequest clientLogin = new LoginRequest("user1@gmail.com", "invalid_password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be possible to create a new delivery person")
    public void  shouldBeAbleToCreateANewDeliveryPerson(){
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                "123456789",
                "12345678910",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.id");
        String name = document.read("$.name");
        String cnpj = document.read("$.cpf");
        String email = document.read("$.email");
        String dateOfBirth = document.read("$.dateOfBirth");
        String typeOfVehicle = document.read("$.typeOfVehicle");
        String telephone = document.read("$.telephone");
        String cnhNumber = document.read("$.cnhNumber");
        String cnhValidity = document.read("$.cnhNumber");
        String vehicleDocument = document.read("$.vehicleDocument");
        Address addressJson = document.read("$.address[0]", Address.class);
        //BankAccount bankAccountJson = document.read("$.bankAccount[0]", BankAccount.class);


        assertThat(id).isNotNull();
        assertThat(email).isEqualTo(deliveryPersonRequest.email());

        assertThat(addressJson).isNotNull();
        assertThat(addressJson).isNotNull();

        assertThat(addressJson.getCep()).isEqualTo("35170-222");
        assertThat(addressJson.getNeighborhood()).isEqualTo("neighborhood");
        assertThat(addressJson.getCity()).isEqualTo("city");
        assertThat(addressJson.getAddress()).isEqualTo("address");
        assertThat(addressJson.getComplement()).isEqualTo("complement");
        assertThat(addressJson.getNumber()).isEqualTo("12");
        assertThat(addressJson.getComplement()).isEqualTo("complement");

        //assertThat(bankAccountJson.getAccount()).isEqualTo("1255");

    }

    @Test
    @DirtiesContext
    @DisplayName("Should be possible to create a new delivery person without cnhNumber")
    public void  shouldBeAbleToCreateANewDeliveryPersonWithoutCNH(){
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                " ",
                "01/02/2020",
                "123456789",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> cnh = documentContext.read("$.cnhNumber");
        assertThat(cnh)
                .containsExactlyInAnyOrder(
                        "CNH obrigatória",
                        "O número da CNH deve conter exatamente 9 dígitos numéricos"
                );


    }


    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the password fields")
    public void shouldReturnAllValidationErrorsInThePasswordFields() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                " ",
                " ",
                LocalDate.of(1999, 1, 2),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                "2102",
                "12345678910",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
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
    @DisplayName("should return all validation errors in the CPF field")
    public void shouldReturnAllValidationErrorsInThePersonResponsibleCPFField() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "0A197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                "2020",
                "12345678910",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> cpf = documentContext.read("$.cpf");
        assertThat(cpf)
                .containsExactlyInAnyOrder(
                        "CPF inválido"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the plate field")
    public void shouldReturnAllValidationErrorsInThePlateField() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2),
                "Carro",
                " ",
                "(11) 95455-4565",
                "123456789",
                "12345678910",
                "12345678910",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> plate = documentContext.read("$.plate");
        assertThat(plate)
                .containsExactlyInAnyOrder(
                        "A placa deve estar no formato XXX-9999",
                        "Verique a placa"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the CNH number fields")
    public void shouldReturnAllValidationErrorsInTheCNHNumberFields() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                "dOCUMENTO DO VEICULO",
                "111",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "details")),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> vehicleDocument = documentContext.read("$.vehicleDocument");
        assertThat(vehicleDocument)
                .containsExactlyInAnyOrder(
                        "O RENAVAM deve conter entre 9 e 11 dígitos numéricos"
                );
    }


}
