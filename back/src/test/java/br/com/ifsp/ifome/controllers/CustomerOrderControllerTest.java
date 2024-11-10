package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.services.CustomerOrderService;
import br.com.ifsp.ifome.services.TokenService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerOrderControllerTest {

    @Autowired
    private TokenService tokenService;

    private String token_cliente;

    private String token_cliente_with_customer_order;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void setUp() {
        this.token_cliente_with_customer_order = tokenService.generateToken("user1@gmail.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.token_cliente = tokenService.generateToken("email1@email.com",  List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));

        mockMvc =MockMvcBuilders.webAppContextSetup(webApplicationContext) .defaultRequest(get("/")
            .characterEncoding("UTF-8")).build();
    }

    private @NotNull HttpHeaders getHttpHeadersClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token_cliente);
        return headers;
    }



}
