package br.com.ifsp.ifome.e2e;

import br.com.ifsp.ifome.dto.request.*;
import br.com.ifsp.ifome.services.TokenService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeliveryWorksIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token_delivery;
    private String token_restaurant;

    @BeforeEach
    public void setUp() {
//        this.token_delivery = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_restaurant = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
        String deliveryAlreadyExists =  tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_DELIVERY")));
//        ResponseEntity<String> response = testRestTemplate.exchange("/api/delivery/info/available/", HttpMethod.PUT, getHttpEntity(deliveryAlreadyExists),  String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldBeRemoveCorrectDishInCart() {
        DeliveryPersonRequest deliveryPersonRequest = generateDelivery();
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/auth/deliveryPerson", deliveryPersonRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        LoginRequest loginRequest = new LoginRequest("teste@teste.com","@Password1");
        response = testRestTemplate.postForEntity("/api/auth/deliveryPerson/login", loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext context = JsonPath.parse(response.getBody());
        String token = context.read("$.data.token");
        response = testRestTemplate.exchange("/api/delivery/order/", HttpMethod.GET, getHttpEntity(token), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        context = JsonPath.parse(response.getBody());
        String message = context.read("$.message");

        assertThat(message).isEqualTo("Buscando pedido!");

        response = testRestTemplate.exchange("/api/delivery/info/available/", HttpMethod.PUT, getHttpEntity(token),  String.class);
        System.err.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        response = testRestTemplate.exchange("/api/delivery/order/status/3", HttpMethod.PUT, getRequestHttpEntityRestaurant(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


    }

    private @NotNull HttpHeaders getHttpHeadersRestaurant() {
        return getHttpHeaders(token_restaurant);
    }

    private @NotNull HttpHeaders getHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private HttpEntity getHttpEntity(String token) {
        return new HttpEntity(getHttpHeaders(token));
    }

    private @NotNull HttpEntity<Object> getRequestHttpEntityRestaurant() {
        HttpHeaders headers = getHttpHeadersRestaurant();
        return new HttpEntity<>(headers);
    }

    private DeliveryPersonRequest generateDelivery() {
        return new DeliveryPersonRequest(
            "Nome entregador",
            "033.197.356-16",
            "teste@teste.com",
            "@Password1",
            "@Password1",
            LocalDate.of(1999, 1, 2).toString(),
            "Carro",
            "DIT-4987",
            "(11) 95455-4565",
            "123456789",
            LocalDate.of(2030, 1, 2),
            "12345678910",
            new AddressRequest("35170-222", "casa 1","neighborhood", "city", "state",
                "address", "complement",
                "12", "condominio","details"),
            new BankAccountRequest("123","1255", "4547-7")
        );
    }
}
