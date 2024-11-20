package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.services.TokenService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
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
public class RestaurantControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String tokenWithOrders;
    private String tokenOtherRestaurant;
    private String tokenValidButRestaurantNotExist;

    private GreenMail greenMail;

    @BeforeEach
    public void setup() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.setUser("teste.ifome@gmail.com", "teste");
        greenMail.setUser("test@example.com", "test@example.com");
        greenMail.start();
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }

    @BeforeEach
    public void setUp() {
        this.tokenWithOrders = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
        this.tokenOtherRestaurant = tokenService.generateToken("email2@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
        this.tokenValidButRestaurantNotExist = tokenService.generateToken("restaurant_not_exists@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

    @Test
    @DisplayName("Should return all restaurants with pagination")
    public void shouldBeAbleReturnAllRestaurantWithPagination() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
            "/api/restaurant/",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should return all restaurants")
    public void shouldBeAbleReturnAllRestaurant() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
            "/api/restaurant/all",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should be return restaurant by id")
    public void shouldBeReturnRestaurant() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
            "/api/restaurant/1",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should not be return restaurant by id does not exist")
    public void shouldNotBeReturnRestaurant() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
            "/api/restaurant/999",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be reverse open with put")
    public void shouldBeReverseOpenPut() {
        HttpEntity<Void> entity = getHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/",
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext document = JsonPath.parse(response.getBody());

        String message = document.read("$.message");
        assertThat(message).isEqualTo("Restaurante fechado com sucesso!");
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be return is open restaurant")
    public void shouldBeIsOpen() {
        HttpEntity<Void> entity = getHttpEntity();

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/1",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext document = JsonPath.parse(response.getBody());

        Boolean isOpen = document.read("$.data.isOpen");
        assertThat(isOpen).isEqualTo(true);
    }

    @Test
    @DisplayName("Should be able to take customer orders")
    public void shouldBeReturnAllOrdersByRestaurant() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/orders",
            HttpMethod.GET,
            getHttpEntity(),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should be able to take customer orders with restaurant does not have orders")
    public void shouldBeReturnAllOrdersByRestaurantWhenRestaurantDoesNotHaveOrders() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/orders",
            HttpMethod.GET,
            getHttpEntity(tokenOtherRestaurant),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should not be able to take customer orders when token valid but restaurant does not exist")
    public void shouldNotBeReturnAllOrdersByRestaurantWhenRestaurantNotExist() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/orders",
            HttpMethod.GET,
            getHttpEntity(tokenValidButRestaurantNotExist),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should not be able to take customer orders")
    public void shouldReturn401ByGetOrders() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/orders",
            HttpMethod.GET,
            getHttpEntityInvalid(),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    @DirtiesContext
    @DisplayName("Should not be possible to update status the order from restaurant ID 1")
    public void shouldBeSuccessByUpdateStateOfTheACustomerOrder() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/order/status/1",
            HttpMethod.PUT,
            getHttpEntity(),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return 403 when trying to update the status of a non-restaurant order")
    public void shouldReturn403ByUpdateStateWhenDoesNot() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/order/status/1",
            HttpMethod.PUT,
            getHttpEntity(tokenOtherRestaurant),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should not be possible to update status the order from other restaurant")
    public void shouldNotBePossibleUpdateStateOfTheOrderFromOtherRestaurant() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/order/status/1",
            HttpMethod.PUT,
            getHttpEntityInvalid(),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should not be possible to upgrade to order status from id 1 to the previous status")
    public void shouldBeAbleUpdateCustomerOrderStatusToThePreviousStatus() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/order/status/1/previous",
            HttpMethod.PUT,
            getHttpEntity(),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should return when trying to update to previous status update the status of a non-restaurant order")
    public void shouldReturn403BUpdatePreviousStateOfTheACustomerOrderWhenDoesNot() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/order/status/1/previous",
            HttpMethod.PUT,
            getHttpEntity(tokenOtherRestaurant),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should not be possible to upgrade to order status from id 1 to the previous status")
    public void shouldReturn401ByUpdatePreviousStateOfTheACustomerOrder() {
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/api/restaurant/order/status/1/previous",
            HttpMethod.PUT,
            getHttpEntityInvalid(),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    private HttpEntity<Void> getHttpEntity(String token) {
        HttpHeaders headers = getHttpHeaders(token);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Void> getHttpEntity() {
        HttpHeaders headers = getHttpHeaders(tokenWithOrders);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Void> getHttpEntityInvalid() {
        HttpHeaders headers = getHttpHeaders("");
        return new HttpEntity<>(headers);
    }


    private HttpHeaders getHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}




