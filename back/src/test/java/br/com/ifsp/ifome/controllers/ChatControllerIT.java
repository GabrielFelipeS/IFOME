package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.MessageRequest;
import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.response.ChatResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;

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

       DocumentContext documentContext = JsonPath.parse(response.getBody());
       int messagesSize = documentContext.read("$.data.messages.length()");

       assertThat(messagesSize).isEqualTo(2);
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

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);
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
    @DirtiesContext
    @DisplayName("Should be able add message in chat client-delivery when client have permission access the chat")
    public void addMessageInChatClientDeliveryWhenClientHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);

        response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.POST,
            postRequestHttpEntityClient("Hello World!"),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.data.content");

        assertThat(message).isEqualTo("Hello World!");

        response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able add message in chat client-delivery when delivery have permission access the chat")
    public void addMessageInChatClientDeliveryWhenDeliveryHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);

        response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.POST,
            postRequestHttpEntityDelivery("Hello World!"),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());
        System.err.println(response.getBody());
        String message = documentContext.read("$.data.content");

        assertThat(message).isEqualTo("Hello World!");

        response = restTemplate.exchange("/api/chat/client/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(3);
    }

    @Test
    @DisplayName("Should be return chat client-restaurant when client have permission access the chat")
    public void getChatClientRestaurantWhenClientHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);
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

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);
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
    @DirtiesContext
    @DisplayName("Should be able add message chat client-restaurant when client have permission access the chat")
    public void addMessageInChatClientRestaurantWhenClientHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);

        response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.POST,
            postRequestHttpEntityClient("Hello World!"),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.data.content");

        assertThat(message).isEqualTo("Hello World!");

         response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

         documentContext = JsonPath.parse(response.getBody());
         messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able add message chat client-restaurant when restaurant have permission access the chat")
    public void addMessageInChatClientRestaurantWhenRestaurantHavePermission() {
        var response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);

        response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.POST,
            postRequestHttpEntityRestaurant("Hello World!"),
            String.class);

        System.err.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.data.content");

        assertThat(message).isEqualTo("Hello World!");

        response = restTemplate.exchange("/api/chat/client/restaurant/4",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(3);
    }

    @Test
    @DisplayName("Should be return chat delivery-restaurant when restaurant have permission access the chat")
    public void getChatRestaurantDeliverytWhenRestaurantHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
                                                HttpMethod.GET,
                                                getRequestHttpEntityRestaurant(),
                                                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);
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

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);
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
    public void getChatRestaurantDeliveryWhenClienttDoesNotHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/5",
            HttpMethod.GET,
            getRequestHttpEntityClient(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able add message in chat delivery-restaurant when restaurant have permission access the chat")
    public void addMessageInChatRestaurantDeliverytWhenRestaurantHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);

        response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.POST,
            postRequestHttpEntityRestaurant("Hello World!"),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.data.content");

        assertThat(message).isEqualTo("Hello World!");

        response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityRestaurant(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(3);
    }

    @Test
    @DirtiesContext
    @DisplayName("Should be able add message in chat delivery-restaurant when delivery have permission access the chat")
    public void addMessageInChatRestaurantDeliverytWhenDeliveryHavePermission() {
        var response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(2);

        response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.POST,
            postRequestHttpEntityDelivery("Hello World!"),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        documentContext = JsonPath.parse(response.getBody());

        String message = documentContext.read("$.data.content");

        assertThat(message).isEqualTo("Hello World!");

        response = restTemplate.exchange("/api/chat/restaurant/delivery/4",
            HttpMethod.GET,
            getRequestHttpEntityDelivery(),
            String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        documentContext = JsonPath.parse(response.getBody());
        messagesSize = documentContext.read("$.data.messages.length()");

        assertThat(messagesSize).isEqualTo(3);
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

    private @NotNull HttpEntity<MessageRequest> postRequestHttpEntityClient(String content) {
        HttpHeaders headers = getHttpHeaders(token_client);
        return new HttpEntity<>(new MessageRequest(content), headers);
    }

    private @NotNull HttpEntity<MessageRequest> postRequestHttpEntityDelivery(String content) {
        HttpHeaders headers = getHttpHeaders(token_delivery);
        return new HttpEntity<>(new MessageRequest(content), headers);
    }

    private @NotNull HttpEntity<MessageRequest> postRequestHttpEntityRestaurant(String content) {
        HttpHeaders headers = getHttpHeaders(token_restaurant);
        return new HttpEntity<>(new MessageRequest(content), headers);
    }
}
