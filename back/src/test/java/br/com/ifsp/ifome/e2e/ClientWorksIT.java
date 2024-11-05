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

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientWorksIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    public void setUp() {
        this.token = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }

    @Test
    @DirtiesContext
    public void shouldBeRemoveCorrectDishInCart() {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntityUpdate = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntityUpdate, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);

        HttpEntity<OrderItemRequest> requestEntity =getOrderItemRequestHttpEntity(3L, 2);

        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        requestEntity =getOrderItemRequestHttpEntity(4L, 2);

        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        requestEntity =getOrderItemRequestHttpEntity(5L, 2);

        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        requestEntity =getOrderItemRequestHttpEntity(3L, 2);

        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntityUpdate, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContextSecond = JsonPath.parse(response.getBody());
        size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(3);

        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> responseUpdate = testRestTemplate.exchange
            ("/api/client/cart/dish/5", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntityUpdate, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContextSecond = JsonPath.parse(response.getBody());
        size = documentContextSecond.read("$.data.orderItems.length()");
        System.err.println(response.getBody());
        assertThat(size).isEqualTo(2);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private @NotNull HttpEntity<OrderItemRequest> getRequestHttpEntity() {
        HttpHeaders headers = getHttpHeaders();
        return new HttpEntity<>(headers);
    }
}
