package br.com.ifsp.ifome.ifome.controllers;


import br.com.ifsp.ifome.ifome.dto.request.ClientRequest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("should be possible to create a new client")
    public void shouldBeAbleToCreateANewClient() {
        ClientRequest client = new ClientRequest("teste@teste.com", "password", "password",
            LocalDate.now(), "cpf", "Casa", "cep", "endereco", "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.id");
        String email = document.read("$.email");
        String dateOfBirth = document.read("$.dateOfBirth");
        String cpf = document.read("$.cpf");
        String typeResidence = document.read("$.typeResidence");
        String cep = document.read("$.cep");
        String address = document.read("$.address");
        String paymentMethods = document.read("$.paymentMethods");

        assertThat(id).isNotNull();
        assertThat(email).isEqualTo(client.email());
        assertThat(dateOfBirth).isEqualTo(client.dateOfBirth().toString());
        assertThat(cpf).isEqualTo(client.cpf());
        assertThat(typeResidence).isEqualTo(client.typeResidence());
        assertThat(cep).isEqualTo(client.cep());
        assertThat(address).isEqualTo(client.address());
        assertThat(paymentMethods).isEqualTo(client.paymentMethods());
    }

    @Test
    @DisplayName("should not be possible to create a new client with already registered email")
    public void shouldReturnErrorWhenCreatingClientWithAlreadyRegisteredEmail() {
        ClientRequest client = new ClientRequest("user1@gmail.com", "password", "password",
            LocalDate.now(), "cpf", "Casa", "cep", "endereco", "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> message = documentContext.read("$.email");

        assertThat(message).containsExactlyInAnyOrder("E-mail já registrado");
    }

}
