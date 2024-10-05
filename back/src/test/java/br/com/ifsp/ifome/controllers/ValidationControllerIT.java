package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.CnpjValidatorRequest;
import br.com.ifsp.ifome.dto.request.CpfValidatorRequest;
import br.com.ifsp.ifome.dto.request.EmailValidatorRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void emailExistsInClient() {
        EmailValidatorRequest emailValidatorRequest = new EmailValidatorRequest("user1@gmail.com");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/client/email", emailValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void emailDoesNotExistInClient() {
        EmailValidatorRequest emailValidatorRequest = new EmailValidatorRequest("naoexiste@gmail.com");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/client/email", emailValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cpfExistsInClient() {
        CpfValidatorRequest cpfValidatorRequest = new CpfValidatorRequest("528.003.140-28");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/client/cpf", cpfValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void cpfDoesNotExistsInClient() {
        CpfValidatorRequest cpfValidatorRequest = new CpfValidatorRequest("922.549.540-40");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/client/cpf", cpfValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cpfExistsInDelivery() {
        CpfValidatorRequest cpfValidatorRequest = new CpfValidatorRequest("528.003.140-28");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/delivery/cpf", cpfValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void cpfDoesNotExistInDelivery() {
        CpfValidatorRequest cpfValidatorRequest = new CpfValidatorRequest("922.549.540-40");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/delivery/cpf", cpfValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void emailExistsInDelivery() {
        EmailValidatorRequest emailValidatorRequest = new EmailValidatorRequest("email1@email.com");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/delivery/email", emailValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void emailDoesNotExistsInDelivery() {
        EmailValidatorRequest emailValidatorRequest = new EmailValidatorRequest("naoexiste@gmail.com");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/delivery/email", emailValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void emailExistsInRestaurant() {
        EmailValidatorRequest emailValidatorRequest = new EmailValidatorRequest("email1@email.com");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/restaurant/email", emailValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }


    @Test
    public void emailDoesNotExistsInRestaurant() {
        EmailValidatorRequest emailValidatorRequest = new EmailValidatorRequest("naoexiste@gmail.com");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/restaurant/email", emailValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cnpjExistsInRestaurant() {
        CnpjValidatorRequest cpfValidatorRequest = new CnpjValidatorRequest("61.610.897/0001-60");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/restaurant/cnpj", cpfValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cnpjDoesNotExistsInRestaurant() {
        CnpjValidatorRequest cpfValidatorRequest = new CnpjValidatorRequest("58.911.612/0001-16");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/validation/restaurant/cnpj", cpfValidatorRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }


}
