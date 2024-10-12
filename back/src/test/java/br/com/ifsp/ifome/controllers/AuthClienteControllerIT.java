package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.request.ForgotPasswordRequest;
import br.com.ifsp.ifome.dto.request.LoginRequest;
import br.com.ifsp.ifome.entities.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WithMockUser
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthClienteControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    public void shouldBeAbleLoginWithValidUser() {
        LoginRequest clientLogin = new LoginRequest("user1@gmail.com", "@Password1");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Object clientResponse = documentContext.read("$.data.user");
        assertThat(clientResponse).isNotNull();

        String token = documentContext.read("$.data.token");
        assertThat(token).isNotNull();

        ResponseEntity<String> responseTokenValidation = restTemplate.postForEntity("/api/auth/token", token, String.class);
        assertThat(responseTokenValidation.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldReturnErrorWhenLoginWithInvalidEmail() {
        LoginRequest clientLogin = new LoginRequest("invalid_email@gmail.com", "@Password1");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldReturnErrorWhenLoginWithInvalidPassword() {
        LoginRequest clientLogin = new LoginRequest("user1@gmail.com", "invalid_password");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client/login", clientLogin, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    @DisplayName("should be possible to create a new client")
    public void shouldBeAbleToCreateANewClient() {
        ClientRequest client = new ClientRequest("Nome completo", "teste@teste.com", "@Password1", "@Password1",
            LocalDate.now().minusYears(18).toString(), "486.086.780-71", "(11) 99248-1491",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "casa","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.data.id");
        String name = document.read("$.data.name");
        String email = document.read("$.data.email");
        String dateOfBirth = document.read("$.data.dateOfBirth");
        String cpf = document.read("$.data.cpf");
        Address addressJson = document.read("$.data.address[0]", Address.class);

        assertThat(id).isNotNull();
        assertThat(name).isEqualTo(client.name());
        assertThat(email).isEqualTo(client.email());
        assertThat(dateOfBirth).isEqualTo(client.dateOfBirth());
        assertThat(cpf).isEqualTo(client.cpf().replaceAll("[^\\d]", ""));

        assertThat(addressJson).isNotNull();
        assertThat(addressJson).isNotNull();

        assertThat(addressJson.getCep()).isEqualTo("35170-222");
        assertThat(addressJson.getNeighborhood()).isEqualTo("neighborhood");
        assertThat(addressJson.getCity()).isEqualTo("city");
        assertThat(addressJson.getAddress()).isEqualTo("address");
        assertThat(addressJson.getComplement()).isEqualTo("complement");
        assertThat(addressJson.getNumber()).isEqualTo("12");
        assertThat(addressJson.getComplement()).isEqualTo("complement");
        assertThat(addressJson.getTypeResidence()).isEqualTo("casa");
    }

    @Test
    @DirtiesContext
    @DisplayName("should be return error with cpf already registred")
    public void shouldReturnErrorWithCpfAlreadyRegistred() {
        ClientRequest client = new ClientRequest("Nome completo", "teste@teste.com", "@Password1", "@Password1",
            LocalDate.now().minusYears(18).toString(), "52800314028", "(11) 99248-1491",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "casa","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.errors.cpf");

        assertThat(message).containsExactlyInAnyOrder("Cpf já cadastrado");
    }


    @Test
    @DirtiesContext
    @DisplayName("should not be possible to create a new client with already registered email")
    public void shouldReturnErrorWhenCreatingClientWithAlreadyRegisteredEmail() {
        ClientRequest client = new ClientRequest("Nome completo","user1@gmail.com", "@Password1", "@Password1",
            LocalDate.now().minusYears(14).toString(), "019.056.440-78", "(11) 99248-1491",new AddressRequest("35170-222", "casa 1", "neighborhood", "city", "state",
            "address",  "complement",
             "12", "condominio","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> message = documentContext.read("$.errors.email");

        assertThat(message).containsExactlyInAnyOrder("E-mail já registrado");
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the password field")
    public void shouldReturnAllValidationErrorsInThePasswordField() {
        ClientRequest client = new ClientRequest("Nome completo","email@gmail.com", " ", " ",
            LocalDate.now().minusYears(18).toString(), "019.056.440-78",  "(11) 99248-1491", new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
            "address",  "complement",
            "12", "condomínio","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
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
    @DisplayName("should return all validation errors in the dateOfBirth field")
    public void shouldReturnAllValidationErrorsInThDateOfBirthField() {
        ClientRequest client = new ClientRequest("Nome completo","email@gmail.com", "@Teste123", "@Teste123",
            LocalDate.now().plusDays(1).toString(), "019.056.440-78",  "(11) 99248-1491", new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
            "address",  "complement",
             "12", "condominio","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> dateOfBirth = documentContext.read("$.errors.dateOfBirth");
        assertThat(dateOfBirth)
            .containsExactlyInAnyOrder(
                "Data de nascimento deve estar no passado",
               "Para cadastro no sistema, é necessário ter pelo menos 13 anos de idade."
            );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return all validation errors in the cpf field")
    public void shouldReturnAllValidationErrorsInTheCPFField() {
        ClientRequest client = new ClientRequest("Nome completo","email@gmail.com", "@Teste123", "@Teste123",
            LocalDate.now().minusYears(18).toString(), "cpf",  "(11) 99248-1491",new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
            "address", "complement",
            "12", "condominio","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> cpf = documentContext.read("$.errors.cpf");
        assertThat(cpf)
            .containsExactlyInAnyOrder(
                "CPF inválido"
            );
    }

    @Test
    @DirtiesContext
    @DisplayName("should return error when registering a client with different password and password confirmation")
    public void shouldReturnErrorWhenCreatingClientWithdifferentPasswordAndPasswordConfirmation() {
        ClientRequest client = new ClientRequest("Nome completo","teste@teste.com", "@Password1", "@Password",
            LocalDate.now().minusYears(18).toString(), "48608678071", "(11) 99248-1491",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address",  "complement",
                 "12", "condominio","details"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(response.getBody());
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);
    }

    @Test
    public void shouldSendEmailResetPassword() throws MessagingException, IOException {
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.setUser("teste.ifome@gmail.com", "teste");
        greenMail.setUser("user1@gmail.com", "test@example.com");
        greenMail.start();
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest("user1@gmail.com");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client/forgot_password", forgotPasswordRequest, String.class);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        greenMail.stop();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(receivedMessages.length).isEqualTo(1);

        Message message = receivedMessages[0];

        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("user1@gmail.com");
        assertThat(message.getSubject()).isEqualTo("Redefinição de senha da conta do IFOME");
    }

    @Test
    public void shouldNotSendEmailResetPasswordWithUserNotExists() {
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.setUser("teste.ifome@gmail.com", "teste");
        greenMail.setUser("test@example.com", "test@example.com");
        greenMail.start();

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest("test@example.com");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/client/forgot_password", forgotPasswordRequest, String.class);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        greenMail.stop();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(receivedMessages.length).isEqualTo(0);
    }

    @Test
    public void shouldResetPassword() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/auth/client/change_password?token=teste",  String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
