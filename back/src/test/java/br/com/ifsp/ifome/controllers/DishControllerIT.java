package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.dto.request.DishRequest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DishControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DirtiesContext
    public void shouldBeAbleToCreateADish(){
        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
                "Lasanha",
               "Massa fresca, molho bolonhesa",
               45.5,
                "Massas",
                "Disponível"

        );

        // Carregar o arquivo de exemplo do classpath
        ClassPathResource fileResource = new ClassPathResource("image.png");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dish", dishRequest);
        body.add("file", fileResource);
        body.add("restaurantId", 1L);

        // Definir os headers da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/auth/dish",
                        requestEntity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.id");
        String name = document.read("$.name");
        String description = document.read("$.description");
        Number price = document.read("$.price");
        String dishCategory = document.read("$.dishCategory");
        String dishImage = document.read("$.dishImage");
        String availability = document.read("$.availability");


        assertThat(id).isNotNull();
        assertThat(name).isEqualTo(dishRequest.name());
        assertThat(price).isEqualTo(dishRequest.price());

    }
}
