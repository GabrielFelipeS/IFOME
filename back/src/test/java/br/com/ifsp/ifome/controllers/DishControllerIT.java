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

import java.util.List;

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

    @Test
    @DirtiesContext
    public void shouldBeAbleToReturnErrorInAvailabilityField(){
        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
                "Lasanha",
                "Massa fresca, molho bolonhesa",
                45.5,
                "Massas",
                "isponível"

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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> availabilityErrors = documentContext.read("$.errors.availability");
        assertThat(availabilityErrors)
                .containsExactlyInAnyOrder(
                        "A disponibilidade deve ser 'Disponível' ou 'Indisponível'"
                );
    }

    @Test
    @DirtiesContext
    public void shouldBeAbleToReturnErrorInPriceField(){
        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
                "Lasanha",
                "Massa fresca, molho bolonhesa",
                0.,
                "Massas",
                "Indisponível"

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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> priceErrors = documentContext.read("$.errors.price");
        assertThat(priceErrors)
                .containsExactlyInAnyOrder(
                        "O preço deve conter apenas valores númericos e positivos"
                );
    }

    @Test
    @DirtiesContext
    public void shouldBeAbleToReturnErrorInNameField(){
        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
                " ",
                "Massa fresca, molho bolonhesa",
                45.,
                "Massas",
                "Indisponível"

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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> nameErrors = documentContext.read("$.errors.name");
        assertThat(nameErrors)
                .containsExactlyInAnyOrder(
                        "O nome do prato é obrigatório"
                );
    }
}


