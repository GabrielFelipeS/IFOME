package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.services.TokenService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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

        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
                "/api/order/customerOrders", HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verifique se o corpo contém os dados corretos
        List<CustomerOrderResponse> orders = response.getBody();
        assertThat(orders).anyMatch(order -> order.status().equals("NOVO") && order.paymentStatus().equals("PENDENTE"));
    }



    @Test
    @DirtiesContext
    public void shouldReturnOrdersMadeToRestaurant() {
        // Obter os cabeçalhos para o restaurante
        HttpHeaders headers = getHttpHeadersRestaurant();

        // Requisição GET para obter todos os pedidos do restaurante
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
                "/api/order/restaurantOrders", HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
        );

        // Verifique se a resposta é OK e se os pedidos estão corretos
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty(); // Verifique se há pedidos

        // Verifique se pelo menos um pedido corresponde aos valores esperados
        List<CustomerOrderResponse> orders = response.getBody();

        // Assegure-se de que pelo menos um dos pedidos existentes atenda aos critérios esperados
        assertThat(orders).anyMatch(order ->
                order.orderPrice().equals(1015.0) && // preço do pedido
                        order.status().equals("NOVO") && // status do pedido
                        order.paymentStatus().equals("PENDENTE") // status de pagamento
        );
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
