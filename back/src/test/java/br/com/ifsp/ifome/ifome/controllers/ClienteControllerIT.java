package br.com.ifsp.ifome.ifome.controllers;


import br.com.ifsp.ifome.ifome.dto.request.ClientRequest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldBeAbleToCreateANewClient() {
        ClientRequest client = new ClientRequest(1L);
        ResponseEntity<String> response = restTemplate.postForEntity("/client", client, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext document = JsonPath.parse(response.getBody());
        Number id = document.read("$.id");

        assertThat(id).isNotNull();
    }
}
