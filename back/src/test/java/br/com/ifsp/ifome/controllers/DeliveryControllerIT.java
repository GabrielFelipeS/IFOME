package br.com.ifsp.ifome.controllers;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeliveryControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TokenService tokenService;

    private String token_delivery_any_order;
    private String token_delivery_none_order;

    @BeforeEach
    public void setUp() {
        this.token_delivery_any_order = "Bearer " + tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_DELIVERY")));
        this.token_delivery_none_order = "Bearer " + tokenService.generateToken("user1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_DELIVERY")));
    }

    @Test
    public void getAllOrderByDeliveryPersonWithNoneOrder() {
        ResponseEntity<String> response = testRestTemplate.exchange("/api/delivery/",
                                                HttpMethod.GET, getHttpEntityWithNoneOrder(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext= JsonPath.parse(response.getBody());
        System.err.println(response.getBody());

        List<String> orders = documentContext.read("$.data");
        assertThat(orders).isNotNull().isEmpty();
    }

    @Test
    public void getAllOrderByDeliveryPersonWithSomeOrder() {
        ResponseEntity<String> response = testRestTemplate.exchange("/api/delivery/",
            HttpMethod.GET, getHttpEntityWithOrder(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext= JsonPath.parse(response.getBody());
        System.err.println(response.getBody());

        List<String> orders = documentContext.read("$.data");
        assertThat(orders).isNotNull().isNotEmpty();
    }

    private HttpEntity getHttpEntityWithNoneOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.token_delivery_none_order);
        return new HttpEntity(headers);
    }

    private HttpEntity getHttpEntityWithOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.token_delivery_any_order);
        return new HttpEntity(headers);
    }
}
