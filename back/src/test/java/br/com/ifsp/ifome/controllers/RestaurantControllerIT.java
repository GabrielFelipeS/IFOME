package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.services.TokenService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    public void setUp() {
        this.token = tokenService.generateToken("email1@email.com");
    }

    @Test
    public void shouldBeAbleReturnAllRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldBeReturnRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/1",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldNotBeReturnRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/999",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldBeReverseOpenPut() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext document = JsonPath.parse(response.getBody());

        String message = document.read("$.message");
        assertThat(message).isEqualTo("Restaurante aberto com sucesso!");
    }

    @Test
    public void shouldBeIsOpen() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/1",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext document = JsonPath.parse(response.getBody());

        Boolean isOpen = document.read("$.data.isOpen");
        assertThat(isOpen).isEqualTo(false);
    }


}




