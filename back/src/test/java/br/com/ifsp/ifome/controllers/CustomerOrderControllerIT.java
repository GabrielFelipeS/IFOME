package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.services.TokenService;
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
    @DirtiesContext
    public void shouldBeAbleGetAllCustomerOrdersByRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente);
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/order/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }



    @Test
    @DirtiesContext
    public void shouldReturnAllCustomerOrders() {
        // Criar o pedido para o cliente
        HttpHeaders headers = getHttpHeadersClient();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> createOrderResponse = testRestTemplate.postForEntity(
                "/api/order/", requestEntity, String.class
        );

        assertThat(createOrderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/order/customerOrders", HttpMethod.GET, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Aqui você pode adicionar mais verificações para o conteúdo da resposta
        // Exemplo: verificar se o corpo contém os dados corretos
        String responseBody = response.getBody();
        assertThat(responseBody).contains("NOVO", "PENDENTE"); // Exemplo de verificações para status do pedido
    }


    @Test
    @DirtiesContext
    public void shouldReturnAllRestaurantOrders() {
        HttpHeaders headers = getHttpHeadersRestaurant(); // Método para obter o cabeçalho do restaurante
        HttpEntity<OrderItemRequest> createOrderRequestEntity = new HttpEntity<>(new OrderItemRequest(3L, 2), headers);

        ResponseEntity<String> createOrderResponse = testRestTemplate.postForEntity(
                "/api/order/", createOrderRequestEntity, String.class
        );

        assertThat(createOrderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/order/restaurantOrders", HttpMethod.GET, requestEntity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String responseBody = response.getBody();
        assertThat(responseBody).contains("NOVO", "PENDENTE"); // Exemplo de verificações para status do pedido
    }


    private @NotNull HttpHeaders getHttpHeadersClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente);
        return headers;
    }

    private @NotNull HttpHeaders getHttpHeadersRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_restaurant);
        return headers;
    }
}
