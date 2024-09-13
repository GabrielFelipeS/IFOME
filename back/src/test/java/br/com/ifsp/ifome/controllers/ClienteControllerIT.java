package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.dto.request.ClientRequest;
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
public class ClienteControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    @DisplayName("should be possible to create a new client")
    public void shouldBeAbleToCreateANewClient() {
        ClientRequest client = new ClientRequest("teste@teste.com", "@Password1", "password",
            LocalDate.now().minusYears(18), "48608678071", "Casa", "cep", "endereco", "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        System.out.println(response.getBody());
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
    @DirtiesContext
    @DisplayName("should not be possible to create a new client with already registered email")
    public void shouldReturnErrorWhenCreatingClientWithAlreadyRegisteredEmail() {
        ClientRequest client = new ClientRequest("user1@gmail.com", "@Password1", "password",
            LocalDate.now().minusYears(18), "019.056.440-78", "Casa", "cep", "endereco", "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<String> message = documentContext.read("$.email");

        assertThat(message).containsExactlyInAnyOrder("E-mail já registrado");
    }

    @Test
    @DirtiesContext
    @DisplayName("")
    public void shouldReturnAllValidationErrorsWhenCreatingClient() {
        ClientRequest client = new ClientRequest("email@.com", " ", "confirmationPassword",
            LocalDate.now().plusYears(1), "cpf", "typeResidence", "cep", "address", "payment_methods");

        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        List<String> emailErrors = documentContext.read("$.email");
        assertThat(emailErrors).containsExactlyInAnyOrder("E-mail inválido");

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

        List<String> dateOfBirth = documentContext.read("$.dateOfBirth");
        assertThat(dateOfBirth)
            .containsExactlyInAnyOrder(
                "Data de nascimento deve estar no passado",
                "É necessário ter pelo menos 18 anos"
            );

        List<String> cpf = documentContext.read("$.cpf");
        assertThat(cpf)
            .containsExactlyInAnyOrder(
                "CPF inválido"
            );
    }

}
