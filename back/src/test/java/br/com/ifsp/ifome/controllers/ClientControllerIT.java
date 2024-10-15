package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.services.TokenService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIT {

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
    public void addDishInCart() {
        OrderRequest orderRequest = new OrderRequest(1L,2);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Object object = documentContext.read("$.client");
        List<OrderItem> orderItems = documentContext.read("$.orderItems");

        System.err.println(object);
        assertThat(object).isNotNull();
        assertThat(orderItems).isNotNull().isNotEmpty();
    }

    @Test
    @DirtiesContext
    public void addDishInCartWhenAlteadyExists() {
        OrderRequest orderRequest = new OrderRequest(1L,2);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Object object = documentContext.read("$.client");
        List<OrderItem> orderItems = documentContext.read("$.orderItems");
        System.err.println(object);
        System.err.println(orderItems);
        assertThat(object).isNotNull();
        assertThat(orderItems).isNotNull().isNotEmpty();
        assertThat(orderItems.size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void addDishInCartWhenDishIdNotExists() {
        OrderRequest orderRequest = new OrderRequest(999L,2);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void addDishInCartWhenClientNotExists() {
        OrderRequest orderRequest = new OrderRequest(1L,2);
        HttpHeaders headers = new HttpHeaders();
        token = tokenService.generateToken("email_nao_existe@gmail.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
