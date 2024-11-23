package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Realizar login")
@RequestBody(
    description = """
        # Detalhes para login
        Campos obrigatórios: E-mail e Senha
      
        Validações dos campos:
      
        ## email:
      
        Tipo: String
      
        Validações:
      
        - Validação sintática do e-mail.
      
        - Não aceita valores nulos, vazios ou contendo apenas espaços.
      
        Anotações: @Email, @NotBlank
      
        ## password:
      
        Tipo: String
      
        Validações:
      
        - Não aceita valores nulos ou vazios.
      
        Anotações: @NotBlank
      
      """,
    required = true,
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ClientRequest.class),
        examples = {
            @ExampleObject(
            name = "login válido com usuário 1",
            description = "Logando com entregador válido",
            value = """
                    {
                      "email" : "email1@email.com",
                      "password": "@Password1"
                    }
                """
        ), @ExampleObject(
            name = "login válido com usuário 2",
            description = "Logando com entregador válido",
            value = """
                    {
                      "email" : "email2@email.com",
                      "password": "@Password1"
                    }
                """
        ),
            @ExampleObject(
                name = "login inválido",
                description =
                """
                    password: senha incorreta
                    """,
                value = """
                    {
                        "email" : "email1@email.com",
                        "password": "senha_incorreta"
                    }
                    """
            )

        }
    )
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Entregador logado com sucesso",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
        examples =  @ExampleObject(value = """
                {
                  "status": "success",
                  "data": {
                    "user": {
                      "id": 1,
                      "name": "João da Silva",
                      "cpf": "52800314028",
                      "email": "email1@email.com",
                      "dateOfBirth": "1985-06-15",
                      "typeOfVehicle": "carro",
                      "plate": "ABC-1234",
                      "telephone": "(11) 98765-4321",
                      "cnhNumber": "12345678910",
                      "cnhValidity": "2026-12-31",
                      "vehicleDocument": "987654321",
                      "address": [
                        {
                          "id": 5,
                          "nameAddress": "Endereço Carlos",
                          "cep": "03090-000",
                          "neighborhood": "Mooca",
                          "city": "São Paulo",
                          "state": "SP",
                          "address": "Rua da Mooca",
                          "number": "300",
                          "complement": "Apto 101",
                          "details": "Próximo ao Parque Villa-Lobos",
                          "typeResidence": "Casa"
                        }
                      ],
                      "bankAccount": {
                        "bank": "Banco do Brasil",
                        "agency": "1234",
                        "account": "00012345-6"
                      }
                    },
                    "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcGlfaWZvbWUiLCJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiZXhwIjoxNzM0MDk4MjI1LCJpYXQiOjE3Mjg4Mzg2MjUsImF1dGhvcml0aWVzIjpbIkRFTElWRVJZIl19.fMK2SlON-Hr9vESt7HyQv87DT0Lvv7ts8qTMxfLsxy2XZN72twDoaU0zbY8W79dkD7qzdJ6pFgO-vEzHl1lYTJ6Lwy8aEX-xhBrEhc-BwFqtkFwhQF5u02syE7W-1lrk6DNq4LPFJ6iuxqBk4z8aWxkVXlbfjvqRKX9Jjxmh1vkxykotYaLaUyaWrfnl-S1uxuDIYccCzaS9ii8NPSTW8UISI9mbwqCrtYjMtTiq-cZV37wCxRpfylWSYRYqV944PwfCN78TmnkfJg7OBDyEFGVZAHPdA1Fv_vymCCEkPKXCHr72BWVdeDtauI-eLfqAZ1H1mQ7rmieKkjBPqTI69g"
                  },
                  "message": "Entregador logado com sucesso"
                }
            """)
      )),
    @ApiResponse(responseCode = "403", description = "Email ou senha incorretos!",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = """
                {
                     "message": "email ou senha incorretos!"
                 }
            """)))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsDeliveryLogin { }
