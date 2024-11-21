package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.services.TokenService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    private String token_client;
    private String token_delivery;
    private String token_restaurant;

    @BeforeEach
    public void setUp() {
        this.token_client = tokenService.generateToken("telha_rina@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_delivery = tokenService.generateToken("email6@email.com",  List.of(new SimpleGrantedAuthority("ROLE_DELIVERY")));
        this.token_restaurant = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

    @Test
    @DisplayName("Should be return chat client-delivery when client have permission access the chat")
    public void getChatClientDeliveryWhenClientHavePermission() {
       var response = restTemplate.exchange("/api/chat/client/delivery/4",
                                               HttpMethod.GET,
                                               getRequestHttpEntityClient(),
                                               String.class);

       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Shouldn´t be return chat client-delivery when client dont´t have permission access the chat")
    public void getChatClientDeliveryWhenClientDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/delivery/2",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should be return chat when delivery have permission access the chat")
    public void getChatWhenDeliveryHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Shouldn´t not be return chat when delivery dont´t have permission access the chat")
    public void getChatWhenDeliveryDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/delivery/2",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Shouldn´t be return chat client-delivery when restaurant dont´t have permission access the chat")
    public void getChatClientDeliveryWhenRestaurantDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/delivery/2",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should be return chat client-restaurant when client have permission access the chat")
    public void getChatClientRestaurantWhenClientHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Shouldn´t be return chat client-restaurant when client dont´t have permission access the chat")
    public void getChatClientRestaurantWhenClientDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/2",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should be return chat client-restaurant when restaurant have permission access the chat")
    public void getChatClientRestaurantWhenRestaurantHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Shouldn´t be return chat client-restaurant when restaurant dont´t have permission access the chat")
    public void getChatClientRestaurantWhenRestaurantDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/5",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Shouldn´t be return chat client-restaurant when delivery dont´t have permission access the chat")
    public void getChatClientDeliverytWhenClientDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/2",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should be return chat delivery-restaurant when restaurant have permission access the chat")
    public void getChatRestaurantDeliverytWhenRestaurantHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
                                                HttpMethod.GET,
                                                getRequestHttpEntityRestaurant(),
                                                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Shouldn´t be return chat delivery-restaurant when restaurant dont´t have permission access the chat")
    public void getChatRestaurantDeliverytWhenRestaurantDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/5",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should be return chat delivery-restaurant when delivery have permission access the chat")
    public void getChatRestaurantDeliverytWhenDeliveryHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Shouldn´t be return chat delivery-restaurant when delivery dont´t have permission access the chat")
    public void getChatRestaurantDeliverytWhenDeliveryDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/2",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    @DisplayName("Shouldn´t be return chat delivery-restaurant when client dont´t have permission access the chat")
    public void getChatWhenClienttDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/5",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private @NotNull HttpEntity<OrderItemRequest> getRequestHttpEntityClient() {
        HttpHeaders headers = getHttpHeaders(token_client);
        return new HttpEntity<>(headers);
    }

    private @NotNull HttpEntity<OrderItemRequest> getRequestHttpEntityDelivery() {
        HttpHeaders headers = getHttpHeaders(token_delivery);
        return new HttpEntity<>(headers);
    }

    private @NotNull HttpEntity<OrderItemRequest> getRequestHttpEntityRestaurant() {
        HttpHeaders headers = getHttpHeaders(token_restaurant);
        return new HttpEntity<>(headers);
    }

    private @NotNull HttpHeaders getHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
