package br.com.ifsp.ifome.e2e;

import br.com.ifsp.ifome.dto.request.*;
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
public class RestaurantWorksIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    public void setUp() {
        this.token = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

    @Test
    @DirtiesContext
    public void shouldBeCreateRestaurantAndLogin() {
        RestaurantRequest restaurant = new RestaurantRequest(
            "Nome Restaurante",
            "test@test.com",
            "@Password1",
            "@Password1",
            "10882594000165",
            new AddressRequest("35170-222", "casa 1","Vila Rio", "Guarulhos", "São Paulo",
                "Av. Salgado Filho", "Em frente a pizzaria",
                "3501", "casa","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")

        );

        ClassPathResource fileResource = new ClassPathResource("image.png");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity(
            "/api/auth/restaurant",
            requestEntity,
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "@Password1");

        response = testRestTemplate.postForEntity(
            "/api/auth/restaurant/login",
            loginRequest,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldBeCreateRestaurantAndLoginAndCreateDish() {
        RestaurantRequest restaurant = new RestaurantRequest(
            "Nome Restaurante",
            "test@test.com",
            "@Password1",
            "@Password1",
            "10882594000165",
            new AddressRequest("35170-222", "casa 1","Vila Rio", "Guarulhos", "São Paulo",
                "Av. Salgado Filho", "Em frente a pizzaria",
                "3501", "casa","details"),
            "(11) 1234-5678",
            "Pizzaria",
            "Dinheiro, Cartão",
            List.of(new OpeningHoursRequest("Segunda-feira","11:00", "23:00"),
                new OpeningHoursRequest("Terça-feira","11:00", "23:00")),
            "responsavel",
            "033.197.356-16",
            new BankAccountRequest("123","1255", "4547-7")

        );

        ClassPathResource fileResource = new ClassPathResource("image.png");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("restaurant", restaurant);
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> response = testRestTemplate.postForEntity(
            "/api/auth/restaurant",
            requestEntity,
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "@Password1");

        response = testRestTemplate.postForEntity(
            "/api/auth/restaurant/login",
            loginRequest,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String tokenLogin = documentContext.read("$.data.token");

        Long restaurantId = 1L; // ID de exemplo
        DishRequest dishRequest = new DishRequest(
            "Lasanha",
            "Massa fresca, molho bolonhesa",
            45.5,
            "Massas",
            "Disponível"

        );

        // Carregar o arquivo de exemplo do classpath
         fileResource = new ClassPathResource("image.png");

        // Criar o mapa de parâmetros para enviar o objeto e o arquivo
        body = new LinkedMultiValueMap<>();
        body.add("dish", dishRequest);
        body.add("file", fileResource);
        body.add("restaurantId", 1L);

        // Definir os headers da requisição
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + tokenLogin);

        HttpEntity<MultiValueMap<String, Object>> requestEntityDish = new HttpEntity<>(body, headers);


        response = testRestTemplate.postForEntity
            ("/api/dish",
                requestEntityDish, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
