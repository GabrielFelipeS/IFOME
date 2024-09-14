package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        examples = {
            @ExampleObject(
            name = "cliente válido",
            description = "Inserindo cliente válido",
            value = """
                    {
                      "email": "novo@cliente.com",
                      "password": "Abc@1234",
                      "confirmationPassword": "Abc@1234",
                      "dateOfBirth": "2000-01-01",
                      "cpf": "211.039.180-44",
                      "phone": "(11) 99248-1491",
                      "address": [
                        {
                          "nameAddress": "casa principal",
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
        ),
            @ExampleObject(
                name = "cliente inválido",
                description =
                """
                    Inserindo cliente inválido
                    cpf: falta 1 número
                    email: não possui @
                    password: não possui letra maiúscula, número e caractere especial
                    address: esta vazio
                """,
                value = """
                    {
                        "cpf": "4860867801",
                        "email" : "gabgmail.com",
                        "password": "password",
                        "confirmationPassword": "password",
                        "dateOfBirth": "2003-04-14",
                        "address": []
                    }
                    """
            )

        }
    )
)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
        examples =  @ExampleObject(value = """
                {
                    "status": "success",
                    "data": {
                         "id": 2,
                         "email": "novo@cliente.com",
                         "dateOfBirth": "2000-01-01",
                         "cpf": "211.039.180-44",
                         "address": [
                             {
                                 "cep": "35170-222",
                                 "neighborhood": "neighborhood",
                                 "city": "Coronel Fabriciano",
                                 "state": "Minas Gerais",
                                 "address": "address",
                                 "number": "12",
                                 "complement": "complement"
                             }
                         ]
                    },
                    "message": "Cliente criado com sucesso"
                }
            """)
      )),
    @ApiResponse(responseCode = "400", description = "Invalid request",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ClientResponse.class),
            examples = @ExampleObject(value = """
            {
                "password": [
                    "Senha precisa conter pelo menos um caractere especial",
                    "Senha precisa conter pelo menos um número",
                    "Senha precisa conter pelo menos um caractere maiúsculo"
                ],
                "address": [
                    "não deve estar vazio"
                ],
                "phone": [
                    "não deve estar em branco"
                ],
                "cpf": [
                    "CPF inválido"
                ],
                "email": [
                    "E-mail inválido"
                ],
                 "clientRequest": [
                     "Senha e confirmar senha não são iguais"
                 ]
            }
            """)))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateClient {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
