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
                "(11) 95455-4565",
                "CNH",
                "dOCUMENTO DO VEICULO",
                List.of(new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                        "address", "complement",
                        "12", "condominio","details")),
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
        String cnh = document.read("$.cnh");
        String vehicleDocument = document.read("$.vehicleDocument");
        Address addressJson = document.read("$.address[0]", Address.class);


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
            List.of(new AddressRequest("35170-222", "casa 1", "neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details")),
            new BankAccountRequest("123", "1255", "4547-7")

        );

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.errors.cpf");

        assertThat(message).containsExactlyInAnyOrder("Cpf já cadastrado");
    }
}
