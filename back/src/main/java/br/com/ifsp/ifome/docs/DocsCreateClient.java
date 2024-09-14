package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Cadastrar um novo cliente")
@RequestBody(
    description = "Detalhes do cliente a ser criado",
    required = true,
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ClientRequest.class),
        examples = @ExampleObject(
            value = """
                {
                    "email": "novo@cliente.com",
                    "password": "Abc@1234",
                    "confirmationPassword": "Abc@1234",
                    "dateOfBirth": "2000-01-01",
                    "cpf": "12345678900",
                    "phone": "(11) 99248-1491",
                    "address": [
                        {
                            "nameAddress": "casa principal"
                            "cep": "35170-222",
                            "neighborhood": "neighborhood",
                            "city": "Coronel Fabriciano",
                            "state": "Minas Gerais",
                            "address": "address", 
                            "complement": "complement",
                            "number": "12",
                            "details": "details"
                        }
                    ]
                }
                """
        )
    )
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateClient {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
