package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.ApiResponse;
import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import br.com.ifsp.ifome.dto.request.UpdateOrderStatusRequest;
import br.com.ifsp.ifome.dto.response.CustomerOrderResponse;
import br.com.ifsp.ifome.entities.CustomerOrder;
import br.com.ifsp.ifome.entities.OrderClientStatus;
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
            ("/api/order",
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
            ("/api/order",
                requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }



    @Test
    @DirtiesContext
    public void shouldReturnAllCustomerOrders() {
        // Criar o pedido para o cliente
        HttpHeaders headers = getHttpHeadersClientWithCustomerOrder();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
                "/api/order/customerOrders", HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verifique se o corpo contém os dados corretos
        List<CustomerOrderResponse> orders = response.getBody();
//        assertThat(orders).anyMatch(order -> order.status().equals("NOVO") && order.paymentStatus().equals("PENDENTE"));
    }



    @Test
    @DirtiesContext
    public void shouldReturnOrdersMadeToRestaurant() {
        HttpHeaders headers = getHttpHeadersRestaurant();

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
                "/api/order/restaurantOrders", HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty(); // Verifique se há pedidos

        List<CustomerOrderResponse> orders = response.getBody();

        assertThat(orders).anyMatch(order ->
                order.orderPrice().equals(1015.0) &&
                        order.paymentStatus().equals("PENDENTE")
        );
    }

    @Test
    @DirtiesContext
    public void shouldFollowOrderStatusSequence() {
        Long orderId = 1L;

        updateOrderStatusAndPrint(orderId, OrderClientStatus.EM_PREPARO);
        updateOrderStatusAndPrint(orderId, OrderClientStatus.PRONTO_PARA_ENTREGA);
        updateOrderStatusAndPrint(orderId, OrderClientStatus.SAIU_PARA_ENTREGA);
        updateOrderStatusAndPrint(orderId, OrderClientStatus.CONCLUIDO);
    }

    private void updateOrderStatusAndPrint(Long orderId, OrderClientStatus expectedStatus) {
        UpdateOrderStatusRequest updateRequest = new UpdateOrderStatusRequest(orderId);
        HttpHeaders headers = getHttpHeadersRestaurant(); // Obtenha os cabeçalhos do restaurante

        ResponseEntity<ApiResponse> response = testRestTemplate.exchange(
                "/api/order/updateStatus/"+orderId, HttpMethod.PUT, new HttpEntity<>(headers), ApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody().message()).isEqualTo("Status atualizado com sucesso!");

        CustomerOrder updatedOrder = customerOrderRepository.findById(orderId).orElseThrow();
        assertThat(updatedOrder.getCurrentOrderClientStatus()).isEqualTo(expectedStatus);

        System.err.println("Status atualizado para: " + updatedOrder.getCurrentOrderClientStatus());
    }

//    @Test
//    @DirtiesContext
//    public void shouldReturnOrdersByCustomerInAscendingOrder() {
//        HttpHeaders headers = getHttpHeadersClientWithCustomerOrder();
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
//                "/api/order/customerOrders", HttpMethod.GET, requestEntity,
//                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        List<CustomerOrderResponse> orders = response.getBody();
//
//        assertThat(orders).isNotEmpty();
//
//        // Log the order dates for debugging
//        for (CustomerOrderResponse order : orders) {
//            System.out.println("Order Date: " + order.orderDate());
//        }
//
//        LocalDateTime previousDate = orders.get(0).orderDate();
//        for (int i = 1; i < orders.size(); i++) {
//            LocalDateTime currentDate = orders.get(i).orderDate();
//            assertThat(currentDate).isAfterOrEqualTo(previousDate);
//            previousDate = currentDate;
//        }
//    }
//
//    @Test
//    @DirtiesContext
//    public void shouldReturnOrdersByCustomerInDescendingOrder() {
//        HttpHeaders headers = getHttpHeadersClientWithCustomerOrder(); // Use a valid client header
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
//                "/api/order/customerOrders", HttpMethod.GET, requestEntity,
//                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        List<CustomerOrderResponse> orders = response.getBody();
//
//        // Verify the orders are in descending order of orderDate
//        assertThat(orders).isNotEmpty();
//
//        // Compare orderDate of each order
//        LocalDateTime previousDate = orders.get(0).orderDate();
//        for (int i = 1; i < orders.size(); i++) {
//            LocalDateTime currentDate = orders.get(i).orderDate();
//            assertThat(currentDate).isBeforeOrEqualTo(previousDate); // Verify current orderDate is before or equal to previous
//            previousDate = currentDate; // Update previousDate for the next comparison
//        }
//    }
//
//    @Test
//    @DirtiesContext
//    public void shouldReturnOrdersByRestaurantInAscendingOrder() {
//        HttpHeaders headers = getHttpHeadersRestaurant();
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
//                "/api/order/restaurantOrders", HttpMethod.GET, requestEntity,
//                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        List<CustomerOrderResponse> orders = response.getBody();
//
//        assertThat(orders).isNotEmpty();
//
//        // Log the order dates for debugging
//        for (CustomerOrderResponse order : orders) {
//            System.out.println("Order Date: " + order.orderDate());
//        }
//
//        LocalDateTime previousDate = orders.get(0).orderDate();
//        for (int i = 1; i < orders.size(); i++) {
//            LocalDateTime currentDate = orders.get(i).orderDate();
//            assertThat(currentDate).isAfterOrEqualTo(previousDate);
//            previousDate = currentDate;
//        }
//    }
//
//    @Test
//    @DirtiesContext
//    public void shouldReturnOrdersByRestaurantInDescendingOrder() {
//        HttpHeaders headers = getHttpHeadersRestaurant(); // Use a valid client header
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<List<CustomerOrderResponse>> response = testRestTemplate.exchange(
//                "/api/order/restaurantOrders", HttpMethod.GET, requestEntity,
//                new ParameterizedTypeReference<List<CustomerOrderResponse>>() {}
//        );
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        List<CustomerOrderResponse> orders = response.getBody();
//
//        // Verify the orders are in descending order of orderDate
//        assertThat(orders).isNotEmpty();
//
//        // Compare orderDate of each order
//        LocalDateTime previousDate = orders.get(0).orderDate();
//        for (int i = 1; i < orders.size(); i++) {
//            LocalDateTime currentDate = orders.get(i).orderDate();
//            assertThat(currentDate).isBeforeOrEqualTo(previousDate); // Verify current orderDate is before or equal to previous
//            previousDate = currentDate; // Update previousDate for the next comparison
//        }
//    }





    private @NotNull HttpHeaders getHttpHeadersClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente);
        return headers;
    }

    private @NotNull HttpHeaders getHttpHeadersClientWithCustomerOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente_with_customer_order);
        return headers;
    }

    private @NotNull HttpHeaders getHttpHeadersRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_restaurant);
        return headers;
    }

}
