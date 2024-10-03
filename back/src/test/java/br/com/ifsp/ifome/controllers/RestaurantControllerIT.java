package br.com.ifsp.ifome.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    @DirtiesContext
    public void shouldBeAbleReturnAllRestaurant() {
//        ResponseEntity<Iterable<RestaurantResponse>> response = testRestTemplate.getForEntity("/api/restaurant/", Iterable<RestaurantResponse>.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}




