package br.com.ifsp.ifome.controllers;


import br.com.ifsp.ifome.dto.request.DishRequest;
import br.com.ifsp.ifome.dto.response.DishResponse;
import br.com.ifsp.ifome.services.TokenService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    public void setUp() {
        this.token = tokenService.generateToken("email1@email.com", List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

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
        headers.set("Authorization", "Bearer " + token);
        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/dish",
                        requestEntity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());

        Number id = document.read("$.data.id");
        String name = document.read("$.data.name");
        String description = document.read("$.data.description");
        Number price = document.read("$.data.price");
        String dishCategory = document.read("$.data.dishCategory");
        String dishImage = document.read("$.data.dishImage");
        String availability = document.read("$.data.availability");


        assertThat(id).isNotNull();
        assertThat(name).isEqualTo(dishRequest.name());
        assertThat(price).isEqualTo(dishRequest.price());

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> get = new HttpEntity<>(headers);
        response = testRestTemplate.exchange("/api/image/"+dishImage, HttpMethod.GET, get, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        headers.set("Authorization", "Bearer " + token);
        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/dish",
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
        headers.set("Authorization", "Bearer " + token);
        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/dish",
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
        headers.set("Authorization", "Bearer " + token);
        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/dish",
                        requestEntity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> nameErrors = documentContext.read("$.errors.name");
        assertThat(nameErrors)
                .containsExactlyInAnyOrder(
                        "O campo \"Nome do prato\" é obrigatório"
                );
    }

    @Test
    @DirtiesContext
    public void shouldBeAbleToReturnErrorInDishCategoryField(){
        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
                "Lasanha",
                "Massa fresca, molho bolonhesa",
                45.,
                "",
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
        headers.set("Authorization", "Bearer " + token);
        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/dish",
                        requestEntity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> dishCategoryErrors = documentContext.read("$.errors.dishCategory");
        assertThat(dishCategoryErrors)
                .containsExactlyInAnyOrder(
                        "O campo \"Categoria\" é obrigatória"
                );
    }

    @Test
    @DirtiesContext
    public void shouldBeAbleToReturnErrorInDescriptionField(){
        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
                "Lasanha",
                " ",
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
        headers.set("Authorization", "Bearer " + token);
        // Criar a entidade Http com o body e os headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity
                ("/api/dish",
                        requestEntity, String.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number countOfInvalidFields = documentContext.read("$.errors.length()");
        assertThat(countOfInvalidFields).isEqualTo(1);

        List<String> descriptionErrors = documentContext.read("$.errors.description");
        assertThat(descriptionErrors)
                .containsExactlyInAnyOrder(
                        "O campo \"Descrição\" é obrigatória"
                );
    }

    @Test
    public void shouldBeReturnAllAvailableDish() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/dish/all", HttpMethod.GET, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int size = documentContext.read("$.data.length()");

        assertThat(size).isEqualTo(7);
    }



    @Test
    public void shouldBeReturnAllAvailableDishWithPagination() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/dish/?page=0&size=1", HttpMethod.GET, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int size = documentContext.read("$.data.content.length()");
        assertThat(size).isEqualTo(1);
    }

    @Test
    public void shouldBeReturnAllAvailableDishById1() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/dish/restaurant/1", HttpMethod.GET, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int size = documentContext.read("$.data.length()");

        assertThat(size).isEqualTo(3);
    }

}


