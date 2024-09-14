package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
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

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    @DisplayName("should be possible to create a new client")
    public void shouldBeAbleToCreateANewClient() throws JsonProcessingException {
        ClientRequest client = new ClientRequest("teste@teste.com", "@Password1", "password",
            LocalDate.now().minusYears(18), "48608678071",
            new AddressRequest("cep", "neighborhood", "city", "state",
                "address", "zipCode", "complement",
                "typeResidence", "number", "floor", "details"), "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.data.id");
        String email = document.read("$.data.email");
        String dateOfBirth = document.read("$.data.dateOfBirth");
        String cpf = document.read("$.data.cpf");
        LinkedHashMap<Object, Object> addressJson = document.read("$.data.address");
        String paymentMethods = document.read("$.data.paymentMethods");

        assertThat(id).isNotNull();
        assertThat(email).isEqualTo(client.email());
        assertThat(dateOfBirth).isEqualTo(client.dateOfBirth().toString());
        assertThat(cpf).isEqualTo(client.cpf());

        assertThat(addressJson).isNotNull().isNotEmpty();
        assertThat(addressJson.get("cep")).isEqualTo("cep");
        assertThat(addressJson.get("neighborhood")).isEqualTo("neighborhood");
        assertThat(addressJson.get("city")).isEqualTo("city");
        assertThat(addressJson.get("address")).isEqualTo("address");
        assertThat(addressJson.get("zipCode")).isEqualTo("zipCode");
        assertThat(addressJson.get("complement")).isEqualTo("complement");
        assertThat(addressJson.get("typeResidence")).isEqualTo("typeResidence");
        assertThat(addressJson.get("number")).isEqualTo("number");
        assertThat(addressJson.get("complement")).isEqualTo("complement");
        assertThat(addressJson.get("floor")).isEqualTo("floor");
    }

    @Test
    @DirtiesContext
    @DisplayName("should not be possible to create a new client with already registered email")
    public void shouldReturnErrorWhenCreatingClientWithAlreadyRegisteredEmail() {
        ClientRequest client = new ClientRequest("user1@gmail.com", "@Password1", "password",
            LocalDate.now().minusYears(14), "019.056.440-78", new AddressRequest("cep", "neighborhood", "city", "state",
            "address", "zipCode", "complement",
            "typeResidence", "number", "walk", "details"), "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.email");

        assertThat(message).containsExactlyInAnyOrder("E-mail já registrado");
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the password field")
    public void shouldReturnAllValidationErrorsInThePasswordField() {
        ClientRequest client = new ClientRequest("email@gmail.com", " ", "confirmationPassword",
            LocalDate.now().minusYears(18), "019.056.440-78",  new AddressRequest("cep", "neighborhood", "city", "state",
            "address", "zipCode", "complement",
            "typeResidence", "number", "walk", "details"), "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

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
    @DisplayName("should return all validation errors in the dateOfBirth field")
    public void shouldReturnAllValidationErrorsInThDateOfBirthField() {
        ClientRequest client = new ClientRequest("email@gmail.com", "@Teste123", "confirmationPassword",
            LocalDate.now().plusDays(1), "019.056.440-78",  new AddressRequest("cep", "neighborhood", "city", "state",
            "address", "zipCode", "complement",
            "typeResidence", "number", "walk", "details"), "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> dateOfBirth = documentContext.read("$.dateOfBirth");
        assertThat(dateOfBirth)
            .containsExactlyInAnyOrder(
                "Data de nascimento deve estar no passado",
                "É necessário ter pelo menos 18 anos"
            );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the cpf field")
    public void shouldReturnAllValidationErrorsInTheCPFField() {
        ClientRequest client = new ClientRequest("email@gmail.com", "@Teste123", "confirmationPassword",
            LocalDate.now().minusYears(18), "cpf",  new AddressRequest("cep", "neighborhood", "city", "state",
            "address", "zipCode", "complement",
            "typeResidence", "number", "walk", "details"), "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> cpf = documentContext.read("$.cpf");
        assertThat(cpf)
            .containsExactlyInAnyOrder(
                "CPF inválido"
            );
    }
}
