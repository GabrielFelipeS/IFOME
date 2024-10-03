package br.com.ifsp.ifome.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void shouldBeAbleReturnAllRestaurant() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/restaurant/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldBeReturnRestaurant() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/restaurant/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldNotBeReturnRestaurant() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/restaurant/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}




