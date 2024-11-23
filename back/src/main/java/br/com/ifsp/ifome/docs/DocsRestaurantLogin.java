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
            name = "login válido",
            description = "logado com restaurante válido com usuário 1",
            value = """
                    {
                      "email" : "email1@email.com",
                      "password": "@Password1"
                    }
                """
        ),     @ExampleObject(
            name = "login válido",
            description = "logado com restaurante válido com usuário 2",
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
    @ApiResponse(responseCode = "200", description = "Logado com sucesso",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
        examples =  @ExampleObject(value = """
            {
              "status": "success",
              "data": {
                "restaurant": {
                  "id": 1,
                  "nameRestaurant": "Açai do monge",
                  "cnpj": "58911612000116",
                  "foodCategory": "Sorveteria",
                  "address": [
                    {
                      "id": 4,
                      "nameAddress": "Endereço Carlos",
                      "cep": "03090-000",
                      "neighborhood": "Mooca",
                      "city": "São Paulo",
                      "state": "SP",
                      "address": "Rua da Mooca",
                      "number": "300",
                      "complement": "Apto 202",
                      "details": "Perto do Parque da Mooca",
                      "typeResidence": "Casa"
                    }
                  ],
                  "telephone": "(11) 1234-5678",
                  "openingHours": [
                    {
                      "id": 1,
                      "dayOfTheWeek": "Segunda-feira",
                      "opening": "08:00",
                      "closing": "22:00"
                    },
                    {
                      "id": 2,
                      "dayOfTheWeek": "Terça-feira",
                      "opening": "08:00",
                      "closing": "22:00"
                    },
                    {
                      "id": 3,
                      "dayOfTheWeek": "Quarta-feira",
                      "opening": "08:00",
                      "closing": "22:00"
                    },
                    {
                      "id": 4,
                      "dayOfTheWeek": "Quinta-feira",
                      "opening": "08:00",
                      "closing": "22:00"
                    },
                    {
                      "id": 5,
                      "dayOfTheWeek": "Sexta-feira",
                      "opening": "08:00",
                      "closing": "23:00"
                    },
                    {
                      "id": 6,
                      "dayOfTheWeek": "Sábado",
                      "opening": "09:00",
                      "closing": "23:00"
                    },
                    {
                      "id": 7,
                      "dayOfTheWeek": "Domingo",
                      "opening": "09:00",
                      "closing": "22:00"
                    }
                  ],
                  "personResponsible": "Nome Responsável",
                  "personResponsibleCPF": "07635915053",
                  "email": "email1@email.com",
                  "paymentMethods": "Dinheiro, Cartão",
                  "restaurantImage": "image.jpg",
                  "bankAccount": {
                    "bank": "Banco do Brasil",
                    "agency": "1234",
                    "account": "00012345-6"
                  },
                  "isOpen": false
                },
                "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcGlfaWZvbWUiLCJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiZXhwIjoxNzM0MDk3NDEwLCJpYXQiOjE3Mjg4Mzc4MTAsImF1dGhvcml0aWVzIjpbIlJFU1RBVVJBTlRfUkVBRCIsIlJFU1RBVVJBTlRfREVMRVRFIiwiUkVTVEFVUkFOVF9DUkVBVEUiLCJSRVNUQVVSQU5UX1VQREFURSIsIlJPTEVfUkVTVEFVUkFOVCJdfQ.JmkkQgoZ8gBhQkJT3MGrCpo_8TDhKfzlmti_HFQie-5LSd2Skf31HD8LSY993dh5JVGA5vAbnKI5kYj-R1e-YEb61B5x8LSKzauyX9Dsh4ONSK2hBMSswVjc_WMPqcioWvrSPjWmkod213bwHlq5TdIak6A_6x8NFxGb5PK4ZQgBSMMn6LI_rPuF54pwhHo7KXQjv1piqHYRL39tpRxcd7JFCCqwHVgYadNTwZJQ8U8A2VtqF35zhsrQeeN4CKNdtM9zQKDq8kLWOYMQKtc3z_cXhxeLtWtSLBFC_CALV4Mhthos_tMfml6u4kzwR55hzZY5eS_Dkw-4RtD5aRMaCw"
              },
              "message": "Restaurante logado com sucesso"
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
public @interface DocsRestaurantLogin { }
