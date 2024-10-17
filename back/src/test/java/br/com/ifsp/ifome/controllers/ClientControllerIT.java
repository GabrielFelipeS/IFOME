package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderRequest;
import br.com.ifsp.ifome.entities.OrderItem;
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

        HttpHeaders headers = getHttpHeaders();

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

    @Test
    @DirtiesContext
    public void updateDishQuantityInCart() {
        HttpEntity<OrderRequest> requestEntity = getOrderRequestHttpEntity();

        ResponseEntity<String> responseInsert = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderRequest orderRequestUpdate = new OrderRequest(3L,10);
        HttpEntity<OrderRequest> requestEntityUpdate = getOrderRequestHttpEntity(orderRequestUpdate);

        ResponseEntity<String> responseUpdate = testRestTemplate.exchange
            ("/api/client", HttpMethod.PUT,
                requestEntityUpdate, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpEntity<OrderRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");
        int quantity = documentContextSecond.read("$.data.orderItems[0].quantity");

        assertThat(size).isEqualTo(1);
        assertThat(quantity).isEqualTo(orderRequestUpdate.quantity());
    }


    @Test
    public void getCartWhenNotExist() {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<OrderRequest> requestEntityUpdate = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client",  HttpMethod.GET, requestEntityUpdate, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);
    }


    @Test
    @DirtiesContext
    public void getCart() {
        HttpEntity<OrderRequest> requestEntityInsert = getOrderRequestHttpEntity();

        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client",
                requestEntityInsert, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpEntity<OrderRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void removeDishInCart() {
        HttpEntity<OrderRequest> requestEntity = getOrderRequestHttpEntity();

        ResponseEntity<String> responseInsert = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderRequest orderRequestUpdate = new OrderRequest(3L,10);
        HttpEntity<OrderRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> responseUpdate = testRestTemplate.exchange
            ("/api/client/3", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);
    }

    @Test
    @DirtiesContext
    public void removeDishWhenDoesNotExistInCart() {
        HttpEntity<OrderRequest> requestEntity = getOrderRequestHttpEntity();

        ResponseEntity<String> responseInsert = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderRequest orderRequestUpdate = new OrderRequest(3L,10);
        HttpEntity<OrderRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/4", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    @Test
    @DirtiesContext
    public void removeDishWhenCartIsEmpty() {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderRequest> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/3", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void removeDishWhenDishDoesNotAvailable() {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderRequest> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/1", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void removeDishWhenDishDoesNotExist() {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderRequest> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/99", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private @NotNull HttpEntity<OrderRequest> getOrderRequestHttpEntity() {
        OrderRequest orderRequest = new OrderRequest(3L,2);
        return getOrderRequestHttpEntity(orderRequest);
    }

    private @NotNull HttpEntity<OrderRequest> getOrderRequestHttpEntity(OrderRequest orderRequest) {
        HttpHeaders headers = getHttpHeaders();
        return new HttpEntity<>(orderRequest, headers);
    }

    private @NotNull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private @NotNull HttpEntity<OrderRequest> getRequestHttpEntity() {
        HttpHeaders headers = getHttpHeaders();
        return new HttpEntity<>(headers);
    }

}
