package br.com.ifsp.ifome.e2e;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
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

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientWorksIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token;
    private String token_restaurant;

    @BeforeEach
    public void setUp() {
        this.token = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_restaurant = tokenService.generateToken("email2@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

    @Test
    @DirtiesContext
    public void shouldBeAbleToPerformAllTheFunctionalitiesForTheCustomer() {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int size = documentContext.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);

        HttpEntity<OrderItemRequest> requestEntityAddDish =getOrderItemRequestHttpEntity(6L, 2);
        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityAddDish, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        requestEntityAddDish =getOrderItemRequestHttpEntity(7L, 2);
        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityAddDish, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        requestEntityAddDish =getOrderItemRequestHttpEntity(8L, 2);
        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityAddDish, String.class);

        assertThat(response.getStatusCode())
            .as("Deveria conseguir insir o prato de id 8")
            .isEqualTo(HttpStatus.CREATED);

        requestEntityAddDish = getOrderItemRequestHttpEntity(6L, 2);
        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityAddDish, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        requestEntityAddDish = getOrderItemRequestHttpEntity(3L, 2);
        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityAddDish, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        size = documentContext.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(3);

        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();

        response = testRestTemplate.exchange
            ("/api/client/cart/dish/7", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        size = documentContext.read("$.data.orderItems.length()");
        Integer quantity = documentContext.read("$.data.totalQuantity");

        assertThat(size).isEqualTo(2);
        assertThat(quantity).isEqualTo(6);

        response = testRestTemplate.exchange("/api/client/order/",  HttpMethod.POST, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());
        Integer orderIdResult = documentContext.read("$.data.orderId");

        response = testRestTemplate.exchange("/api/restaurant/orders",  HttpMethod.GET, getRequestHttpEntity(token_restaurant), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        Integer orderId = documentContext.read("$.data[0].orderId");

        assertThat(orderId).isEqualTo(orderIdResult);
    }

    private @NotNull HttpEntity<OrderItemRequest> getOrderItemRequestHttpEntity() {
        return getOrderItemRequestHttpEntity(3l, 2);
    }

    private @NotNull HttpEntity<OrderItemRequest> getOrderItemRequestHttpEntity(Long dishId, Integer quantity) {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(dishId,quantity, null);
        return getOrderItemRequestHttpEntity(OrderItemRequest);
    }

    private @NotNull HttpEntity<OrderItemRequest> getOrderItemRequestHttpEntity(OrderItemRequest OrderItemRequest) {
        HttpHeaders headers = getHttpHeaders();
        return new HttpEntity<>(OrderItemRequest, headers);
    }

    private @NotNull HttpHeaders getHttpHeaders() {
        return getHttpHeaders(token);
    }

    private @NotNull HttpHeaders getHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private @NotNull HttpEntity<OrderItemRequest> getRequestHttpEntity() {
        HttpHeaders headers = getHttpHeaders();
        return new HttpEntity<>(headers);
    }

    private @NotNull HttpEntity<OrderItemRequest> getRequestHttpEntity(String token) {
        HttpHeaders headers = getHttpHeaders(token);
        return new HttpEntity<>(headers);
    }
}
