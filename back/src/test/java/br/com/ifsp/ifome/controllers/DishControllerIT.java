package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.dto.request.DishRequest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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
public class DishControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DirtiesContext
    public void shouldBeAbleToCreateADish(){
        DishRequest dishRequest = new DishRequest(
                "Lasanha",
               45.5,
                "Massas",
                "image.png",
                "Disponível"

        );
        ResponseEntity<String> response = testRestTemplate.postForEntity("/dish", dishRequest, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.id");
        String name = document.read("$.name");
        String price = document.read("$.price");
        String dishCategory = document.read("$.dishCategory");
        String dishImage = document.read("$.dishImage");
        String dishAvailability = document.read("$.dishAvailability");


        assertThat(id).isNotNull();

    }
}
