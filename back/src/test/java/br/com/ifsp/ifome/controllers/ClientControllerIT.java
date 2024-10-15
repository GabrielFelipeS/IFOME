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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        OrderRequest orderRequest = new OrderRequest(3L,2);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Object object = documentContext.read("$.data.client");
        List<OrderItem> orderItems = documentContext.read("$.data.orderItems");

        assertThat(object).isNotNull();
        assertThat(orderItems).isNotNull().isNotEmpty();
    }

    @Test
    @DirtiesContext
    public void addDishInCartWhenDishIsNotAvailable() {
        OrderRequest orderRequest = new OrderRequest(1L,2);
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
    public void addDishInCartWhenAlteadyExists() {
        OrderRequest orderRequest = new OrderRequest(3L,2);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext documentContextFirst = JsonPath.parse(responseFirst.getBody());
        Integer quantityFirst = documentContextFirst.read("$.data.orderItems[0].quantity");
        Double unitPriceFirst = documentContextFirst.read("$.data.orderItems[0].unitPrice");
        Double totalPriceFirst = documentContextFirst.read("$.data.totalprice");

        assertThat(quantityFirst).isEqualTo(2);
        assertThat((quantityFirst * unitPriceFirst)).isEqualTo(totalPriceFirst);


        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        System.out.println(response.getBody());
        Object object = documentContextSecond.read("$.data.client");
        List<OrderItem> orderItems = documentContextSecond.read("$.data.orderItems");
        Integer quantitySecond = documentContextSecond.read("$.data.orderItems[0].quantity");
        Double unitPriceSecond = documentContextSecond.read("$.data.orderItems[0].unitPrice");
        Double  totalPriceSecond = documentContextSecond.read("$.data.totalprice");

        assertThat(object).isNotNull();
        assertThat(orderItems).isNotNull().isNotEmpty();
        assertThat(orderItems.size()).isEqualTo(1);
        assertThat(quantitySecond).isEqualTo(4);
        assertThat((quantitySecond * unitPriceSecond)).isEqualTo(totalPriceSecond);
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

    @Test
    @DirtiesContext
    public void addDishFromAnotherRestaurant() {
        double price_dish = 29.90;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        OrderRequest orderRequest = new OrderRequest(3L,2);
        HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderRequest orderRequestAnotherRestaurant = new OrderRequest(8L,2);
        HttpEntity<OrderRequest> requestEntityAnotherRestaurant  = new HttpEntity<>(orderRequestAnotherRestaurant, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntityAnotherRestaurant, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Só pde ser incluido pratos do mesmo restaurante no pedido");
    }
}
