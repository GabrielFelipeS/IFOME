package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.entities.OrderItem;
import br.com.ifsp.ifome.services.TokenService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        this.token = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able add dish in cart")
    public void addDishInCart() {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(3L,2, null);
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        List<OrderItem> orderItems = documentContext.read("$.data.orderItems");

        assertThat(orderItems).isNotNull().isNotEmpty();
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able add dish in cart when dish is not available")
    public void addDishInCartWhenDishIsNotAvailable() {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(1L,2, null);
        HttpHeaders headers =  getHttpHeaders();

        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able add dish in cart when dish already exists in cart")
    public void addDishInCartWhenAlteadyExists() {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(3L,2, null);
        HttpHeaders headers =  getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);
        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext documentContextFirst = JsonPath.parse(responseFirst.getBody());
        Integer quantityFirst = documentContextFirst.read("$.data.orderItems[0].quantity");
        Double unitPriceFirst = documentContextFirst.read("$.data.orderItems[0].unitPrice");
        Double totalPriceFirst = documentContextFirst.read("$.data.totalPrice");
        Double freigth = documentContextFirst.read("$.data.freigth");

        assertThat(quantityFirst).isEqualTo(2);
        assertThat((quantityFirst * unitPriceFirst + freigth)).isEqualTo(totalPriceFirst);


        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.err.println(response.getBody());
        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        List<OrderItem> orderItems = documentContextSecond.read("$.data.orderItems");
        Integer quantitySecond = documentContextSecond.read("$.data.orderItems[0].quantity");
        Double unitPriceSecond = documentContextSecond.read("$.data.orderItems[0].unitPrice");
        Double  totalPriceSecond = documentContextSecond.read("$.data.totalPrice");
        Double  freigthSecond = documentContextSecond.read("$.data.freigth");
        assertThat(orderItems).isNotNull().isNotEmpty();

        assertThat(orderItems.size()).isEqualTo(1);

        assertThat(quantitySecond).isEqualTo(4);

        assertThat((quantitySecond * unitPriceSecond + freigthSecond)).isEqualTo(totalPriceSecond);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able add dish in cart when dish is not exists")
    public void addDishInCartWhenDishIdNotExists() {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(999L,2, null);
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able add dish in cart when client not exist")
    public void addDishInCartWhenClientNotExists() {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(1L,2, null);
        String token = tokenService.generateToken("email_nao_existe@gmail.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able add dish in cart when dish is from another restaurant")
    public void addDishFromAnotherRestaurant() {
        double price_dish = 29.90;

        HttpHeaders headers = getHttpHeaders();

        OrderItemRequest OrderItemRequest = new OrderItemRequest(3L,2, null);
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderItemRequest OrderItemRequestAnotherRestaurant = new OrderItemRequest(8L,2, null);
        HttpEntity<OrderItemRequest> requestEntityAnotherRestaurant  = new HttpEntity<>(OrderItemRequestAnotherRestaurant, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityAnotherRestaurant, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Só pode ser incluido pratos do mesmo restaurante no pedido");
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able update dish quantity in cart")
    public void updateDishQuantityInCart() {
        HttpEntity<OrderItemRequest> requestEntity = getOrderItemRequestHttpEntity();

        ResponseEntity<String> responseInsert = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderItemRequest OrderItemRequestUpdate = new OrderItemRequest(3L,10, null);
        HttpEntity<OrderItemRequest> requestEntityUpdate = getOrderItemRequestHttpEntity(OrderItemRequestUpdate);

        ResponseEntity<String> responseUpdate = testRestTemplate.exchange
            ("/api/client/cart/dish/", HttpMethod.PUT,
                requestEntityUpdate, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");
        int quantity = documentContextSecond.read("$.data.orderItems[0].quantity");

        assertThat(size).isEqualTo(1);
        assertThat(quantity).isEqualTo(OrderItemRequestUpdate.quantity());
    }


    @Test
    @DisplayName("Should be able get cart when not exist")
    public void getCartWhenNotExist() {
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntityUpdate = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntityUpdate, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);
    }


    @Test
    @DirtiesContext
    @DisplayName("Should be able get cart when it is exists")
    public void getCartWhenAlreadyExst() {
        HttpEntity<OrderItemRequest> requestEntityInsert = getOrderItemRequestHttpEntity();

        ResponseEntity<String> responseFirst = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntityInsert, String.class);

        assertThat(responseFirst.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able remove dish in cart")
    public void removeDishInCart() {
        HttpEntity<OrderItemRequest> requestEntity = getOrderItemRequestHttpEntity();

        ResponseEntity<String> responseInsert = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> responseUpdate = testRestTemplate.exchange
            ("/api/client/cart/dish/3", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able remove dish in cart when it is exists")
    public void removeDishWhenDoesNotExistInCart() {
        HttpEntity<OrderItemRequest> requestEntity = getOrderItemRequestHttpEntity();

        ResponseEntity<String> responseInsert = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderItemRequest OrderItemRequestUpdate = new OrderItemRequest(3L,10, null);
        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/cart/dish/4", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    @Test
    @DirtiesContext
    @DisplayName("Should be not able remove dish in cart when cart is empty")
    public void removeDishWhenCartIsEmpty() {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderItemRequest> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/dish/3", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able remove dish in cart when dish is does not exist in cart")
    public void removeDishWhenDishDoesNotAvailableInCart() {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderItemRequest> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/dish/1", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be not able remove dish in cart when dish is does not exist")
    public void removeDishWhenDishDoesNotExist() {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<OrderItemRequest> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange
            ("/api/client/dish/99", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return empty cart before create create customer order")
    public void shouldReturnEmptyCartBeforeCreateCustomerOrder() {
        OrderItemRequest OrderItemRequest = new OrderItemRequest(3L,2, null);
        HttpHeaders headers =  getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);
        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        requestEntity = new HttpEntity<>(headers);

        response = testRestTemplate.postForEntity
            ("/api/order",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able clear cart when have two dishes")
    public void clearCart() {
        HttpEntity<OrderItemRequest> requestHttpEntity = getRequestHttpEntity();
        OrderItemRequest OrderItemRequest = new OrderItemRequest(3L,2, null);
        HttpHeaders headers =  getHttpHeaders();
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        OrderItemRequest = new OrderItemRequest(4L,2, null);
        requestEntity = new HttpEntity<>(OrderItemRequest, headers);

        response = testRestTemplate.postForEntity
            ("/api/client/cart/dish/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContextSecond = JsonPath.parse(response.getBody());
        int size = documentContextSecond.read("$.data.orderItems.length()");
        int quantity = documentContextSecond.read("$.data.totalQuantity");

        assertThat(size).isEqualTo(2);
        assertThat(quantity).isEqualTo(6);

        ResponseEntity<String> responseUpdate = testRestTemplate.exchange
            ("/api/client/cart/clear/", HttpMethod.DELETE,
                requestHttpEntity, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        response = testRestTemplate.exchange("/api/client/cart",  HttpMethod.GET, requestHttpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContextSecond = JsonPath.parse(response.getBody());
        size = documentContextSecond.read("$.data.orderItems.length()");

        assertThat(size).isEqualTo(0);
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
