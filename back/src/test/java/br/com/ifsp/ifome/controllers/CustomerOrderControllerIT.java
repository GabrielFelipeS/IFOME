package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.request.UpdateOrderStatusRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderStatus;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerOrderControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private TokenService tokenService;

    private String token_cliente;
    private String token_restaurant;
    private String token_cliente_with_customer_order;

    @BeforeEach
    public void setUp() {
        this.token_cliente_with_customer_order = tokenService.generateToken("user1@gmail.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_cliente = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_restaurant= tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
    }

//    @Test
//    @DirtiesContext
//    public void shouldBeAbleGetAllCustomerOrdersByRestaurant() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token_cliente);
//        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = testRestTemplate.postForEntity
//            ("/api/order",
//                requestEntity, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//    }

    @Test
    @DirtiesContext
    public void shouldNotBeAbleCreateCustomerOrderWithCartEmpty() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente);
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/order/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DirtiesContext
    public void shouldNotBeAbleCreateCustomerOrderWithCartNotExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente_with_customer_order);
        HttpEntity<OrderItemRequest> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity
            ("/api/order/",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
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

    @Test
    @DirtiesContext
    public void shouldUpdateOrderStatusAsRestaurant() {
        // Primeiro, vamos criar um pedido no banco de dados
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.NOVO);
        // Defina outros atributos do pedido, como preço, cart, restaurant, etc.
        customerOrderRepository.save(order); // Salve o pedido

        Long orderId = order.getId(); // Obtenha o ID do pedido criado

        CustomerOrder existingOrder = customerOrderRepository.findById(orderId).orElseThrow();
        System.out.println("Status anterior: " + existingOrder.getStatus());

        // Agora, vamos atualizar o status usando o token de restaurante
        UpdateOrderStatusRequest updateRequest = new UpdateOrderStatusRequest(orderId, OrderStatus.ACEITO);
        HttpHeaders headers = getHttpHeadersRestaurant(); // Obtenha os cabeçalhos do restaurante
        ResponseEntity<ApiResponse> response = testRestTemplate.exchange(
                "/api/order/updateStatus", HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ApiResponse.class
        );

        // Verifique se a atualização foi bem-sucedida
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().message()).isEqualTo("Status atualizado com sucesso!");

        // Verifique se o status foi realmente atualizado no banco de dados
        CustomerOrder updatedOrder = customerOrderRepository.findById(orderId).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.ACEITO);

        System.out.println("Novo status do pedido: " + updatedOrder.getStatus());

    }

    @Test
    @DirtiesContext
    public void shouldNotAllowClientToUpdateOrderStatus() {
        // Criar um pedido no banco de dados
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.NOVO);
        // Defina outros atributos do pedido, como preço, cart, restaurant, etc.
        customerOrderRepository.save(order); // Salve o pedido

        Long orderId = order.getId(); // Obtenha o ID do pedido criado

        UpdateOrderStatusRequest updateRequest = new UpdateOrderStatusRequest(orderId, OrderStatus.EM_PREPARO);
        HttpHeaders headers = getHttpHeadersClient(); // Obtenha os cabeçalhos do cliente

        ResponseEntity<ApiResponse> response = testRestTemplate.exchange(
                "/api/order/updateStatus", HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DirtiesContext
    public void shouldFollowOrderStatusSequence() {
        // Criar um pedido no banco de dados
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.NOVO);
        customerOrderRepository.save(order); // Salve o pedido

        Long orderId = order.getId(); // Obtenha o ID do pedido criado

        // Verificar a sequência de status
        updateOrderStatusAndAssert(orderId, OrderStatus.ACEITO);
        updateOrderStatusAndAssert(orderId, OrderStatus.EM_PREPARO);
        updateOrderStatusAndAssert(orderId, OrderStatus.PRONTO_PARA_ENTREGA);
        updateOrderStatusAndAssert(orderId, OrderStatus.SAIU_PARA_ENTREGA);
        updateOrderStatusAndAssert(orderId, OrderStatus.CONCLUIDO);
    }

    private void updateOrderStatusAndAssert(Long orderId, OrderStatus expectedStatus) {
        UpdateOrderStatusRequest updateRequest = new UpdateOrderStatusRequest(orderId, expectedStatus);
        HttpHeaders headers = getHttpHeadersRestaurant(); // Obtenha os cabeçalhos do restaurante

        ResponseEntity<ApiResponse> response = testRestTemplate.exchange(
                "/api/order/updateStatus", HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().message()).isEqualTo("Status atualizado com sucesso!");

        CustomerOrder updatedOrder = customerOrderRepository.findById(orderId).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(expectedStatus);
    }

    @Test
    @DirtiesContext
    void shouldNotAllowStatusUpdateOutOfOrder() {
        // Primeiro, vamos criar um pedido no banco de dados
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.NOVO);
        // Defina outros atributos do pedido, como preço, cart, restaurant, etc.
        customerOrderRepository.save(order); // Salve o pedido

        Long orderId = order.getId(); // Obtenha o ID do pedido criado

        CustomerOrder existingOrder = customerOrderRepository.findById(orderId).orElseThrow();
        System.out.println("Status anterior: " + existingOrder.getStatus());

        // Agora, vamos atualizar o status usando o token de restaurante
        UpdateOrderStatusRequest updateRequest = new UpdateOrderStatusRequest(orderId, OrderStatus.SAIU_PARA_ENTREGA);
        HttpHeaders headers = getHttpHeadersRestaurant(); // Obtenha os cabeçalhos do restaurante
        ResponseEntity<ApiResponse> response = testRestTemplate.exchange(
                "/api/order/updateStatus", HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), ApiResponse.class
        );


        // Verificar o resultado
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transição de status inválida. O status deve ser atualizado em ordem.", response.getBody().message());
        System.out.println(response.getStatusCode() + response.getBody().message());
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
