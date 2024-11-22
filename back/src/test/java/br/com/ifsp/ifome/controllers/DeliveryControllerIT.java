package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.dto.request.CoordinatesRequest;
import br.com.ifsp.ifome.repositories.CustomerOrderRepository;
import br.com.ifsp.ifome.repositories.RefuseCustomerOrderRepository;
import br.com.ifsp.ifome.services.ChoiceDeliveryService;
import br.com.ifsp.ifome.services.CustomerOrderService;
import br.com.ifsp.ifome.services.DeliveryService;
import br.com.ifsp.ifome.services.TokenService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeliveryControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token_delivery_none_order;
    private String token_restaurant;
    private String token_other_delivery;

    @Autowired
    private ChoiceDeliveryService choiceDeliveryService;
    @Autowired
    private RefuseCustomerOrderRepository refuseCustomerOrderRepository;
    @Autowired
    private DeliveryService deliveryService;


    @BeforeEach
    public void setUp() {
        this.token_restaurant = "Bearer " + tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_RESTAURANT")));
        this.token_delivery_none_order = "Bearer " + tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_DELIVERY")));
        this.token_other_delivery = "Bearer " + tokenService.generateToken("email2@email.com",  List.of(new SimpleGrantedAuthority("ROLE_DELIVERY")));
    }

    @Test
    @DirtiesContext
    public void getAllOrderByDeliveryPersonWithNoneOrder() {
        ResponseEntity<String> response = testRestTemplate.exchange("/api/delivery/order/",
                                                HttpMethod.GET, getHttpEntityWithNoneOrder(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext= JsonPath.parse(response.getBody());
        System.err.println(response.getBody());

        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Buscando pedido!" );
    }

//    @Test
//    @DirtiesContext
//    public void getAllOrderByDeliveryPersonWithSomeOrder() {
//        ResponseEntity<String> response = testRestTemplate.exchange("/api/restaurant/order/status/3",
//            HttpMethod.PUT, getHttpEntityRestaurant(), String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        response = testRestTemplate.exchange("/api/delivery/order/",
//            HttpMethod.GET, getHttpEntityWithNoneOrder(), String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        DocumentContext documentContext= JsonPath.parse(response.getBody());
//        System.err.println(response.getBody());
//
//        String message = documentContext.read("$.message");
//        assertThat(message).isEqualTo("Pedido atual pego com sucesso!");
//    }



    private HttpEntity getHttpEntityRestaurant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.token_restaurant);
        return new HttpEntity(headers);
    }

    private HttpEntity getHttpEntityWithNoneOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.token_delivery_none_order);
        return new HttpEntity(headers);
    }

}
