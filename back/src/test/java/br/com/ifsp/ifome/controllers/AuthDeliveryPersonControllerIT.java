package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.BankAccountRequest;
import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.entities.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthDeliveryPersonControllerIT {
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

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldReturnErrorWhenLoginWithInvalidPassword() {
        LoginRequest clientLogin = new LoginRequest("user1@gmail.com", "invalid_password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
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
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2030, 1, 2),
                "12345678910",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.data.id");
        String name = document.read("$.data.name");
        String cpf = document.read("$.data.cpf");
        String email = document.read("$.data.email");
        String dateOfBirth = document.read("$.data.dateOfBirth");
        String typeOfVehicle = document.read("$.data.typeOfVehicle");
        String telephone = document.read("$.data.telephone");
        String cnhNumber = document.read("$.data.cnhNumber");
        String cnhValidity = document.read("$.data.cnhNumber");
        String vehicleDocument = document.read("$.data.vehicleDocument");
        Address addressJson = document.read("$.data.address[0]", Address.class);
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
        assertThat(addressJson.getTypeResidence()).isEqualTo("condominio");
    }

    @Test
    @DirtiesContext
    @DisplayName("should be return error with cpf already registred")
    public void shouldReturnErrorWithCpfAlreadyRegistred() throws JsonProcessingException {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
            "Nome entregador",
            "528.003.140-28",
            "email@email.com",
            "@Senha1",
            "@Senha1",
            LocalDate.of(1999, 1, 2).toString(),
            "Carro",
            "(11) 95455-4565",
            "CNH",
            "dOCUMENTO DO VEICULO",
            LocalDate.of(2030, 1, 2),
            "123456789",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                    "address", "complement",
                    "12", "condominio","details"),
            new BankAccountRequest("123","1255", "4547-7")

    );

        //assertThat(bankAccountJson.getAccount()).isEqualTo("1255");

    }

    @Test
    @DirtiesContext
    @DisplayName("Should be possible to not create a new delivery person without cnhNumber")
    public void  shouldBeAbleToNoTCreateANewDeliveryPersonWithoutCNH(){
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                " ",
                LocalDate.of(2030, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> cnh = documentContext.read("$.errors.cnhNumber");
        assertThat(cnh)
                .containsExactlyInAnyOrder(
                        "O campo \"Número do CNH\" é obrigatório",
                        "O número da CNH deve conter entre 9 e 11 dígitos numéricos"
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
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2030, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(2);

        List<String> passwordErrors = documentContext.read("$.errors.password");
        assertThat(passwordErrors)
                .containsExactlyInAnyOrder(
                        "O campo \"Senha\" é obrigatório",
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
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2030, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> cpf = documentContext.read("$.errors.cpf");
        assertThat(cpf)
                .containsExactlyInAnyOrder(
                        "O campo \"CPF\" deve estar no formato: XXX.XXX.XXX-XX"
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
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                " ",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2030, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> plate = documentContext.read("$.errors.plate");
        assertThat(plate)
                .containsExactlyInAnyOrder(
                        "A placa deve estar no formato XXX-9999",
                        "O campo \"Placa\" é obrigatório"
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
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "12345A",
                LocalDate.of(2030, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> cnhNumber = documentContext.read("$.errors.cnhNumber");
        assertThat(cnhNumber)
                .containsExactlyInAnyOrder(
                        "O número da CNH deve conter entre 9 e 11 dígitos numéricos"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the CNHN Validity fields")
    public void shouldReturnAllValidationErrorsInTheCNHNValidityFields() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2024, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> cnhValidity = documentContext.read("$.errors.cnhValidity");
        assertThat(cnhValidity )
                .containsExactlyInAnyOrder(
                        "CNH fora da validade"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the VehicleDocument field")
    public void shouldReturnAllValidationErrorsInTheVehicleDocumentField() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2030, 1, 2),
                "111",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> vehicleDocument = documentContext.read("$.errors.vehicleDocument");
        assertThat(vehicleDocument)
                .containsExactlyInAnyOrder(
                        "O RENAVAM deve conter entre 9 e 11 dígitos numéricos"
                );
    }

    @Test
    @DirtiesContext
    @DisplayName("should detect a under age delivery person")
    public void shouldDetectAUnderAgeDeliveryPerson() {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(2011, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "123456789",
                LocalDate.of(2030, 1, 2),
                "111456789",
                new AddressRequest(
                        "35170-222",
                        "casa 1",
                        "neighborhood",
                        "city",
                        "state",
                        "address",
                        "complement",
                        "12",
                        "condominio",
                        "details"),
                new BankAccountRequest("123","1255", "4547-7")

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> dateOfBirth = documentContext.read("$.errors.dateOfBirth");
        assertThat(dateOfBirth)
                .containsExactlyInAnyOrder(
                        "Para cadastro no sistema, é necessário ter pelo menos 18 anos de idade."
                );
    }


    @Test
    @DirtiesContext
    @DisplayName("should be return error with cnh already registred")
    public void shouldReturnErrorWithCnhAlreadyRegistred() throws JsonProcessingException {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest(
                "Nome entregador",
                "033.197.356-16",
                "email@email.com",
                "@Senha1",
                "@Senha1",
                LocalDate.of(1999, 1, 2).toString(),
                "Carro",
                "DIT-4987",
                "(11) 95455-4565",
                "12345678910",
                LocalDate.of(2030, 1, 2),
                "123456789",
                new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details"),
                new BankAccountRequest("123","1255", "4547-7")

        );

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);


        List<String> cnhNumber = documentContext.read("$.errors.cnhNumber");
        assertThat(cnhNumber)
                .containsExactlyInAnyOrder(
                        "CNH já registrada"
                );
    }





}
