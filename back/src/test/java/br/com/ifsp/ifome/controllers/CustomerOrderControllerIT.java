package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.services.TokenService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerOrderControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token_cliente;
    private String token_restaurant;

    @BeforeEach
    public void setUp() {
        this.token_cliente = tokenService.generateToken("user1@gmail.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_restaurant= tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

    @Test
    public void shouldBeAbleGetAllCustomerOrdersByRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente);
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/order",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);


    }

    private @NotNull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

}
