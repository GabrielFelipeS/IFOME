package br.com.ifsp.ifome.validation.validators.anotations;

import br.com.ifsp.ifome.validation.anotations.CEP;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CEPValidator implements ConstraintValidator<CEP,String> {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public CEPValidator(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    @Override
    public void initialize(CEP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String url = String.format("https://viacep.com.br/ws/%s/json/", cep);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode node = objectMapper.readTree(response);
             return node.has("cep") && node.get("cep").asText().equals(cep);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
