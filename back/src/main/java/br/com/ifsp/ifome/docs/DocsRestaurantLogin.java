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
            description = "logado com restaurante válido",
            value = """
                    {
                      "email" : "email1@email.com",
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
                   "status": "sucess",
                   "data": {
                     "restaurant": {
                       "id": 1,
                       "nameRestaurant": "Nome Restaurante",
                       "cnpj": "58.911.612/0001-16",
                       "foodCategory": "Pizzaria",
                       "address": [],
                       "telephone": "(11) 1234-5678",
                       "openingHours": [],
                       "personResponsible": "Nome Responsável",
                       "personResponsibleCPF": null,
                       "email": "email1@email.com",
                       "paymentMethods": "Dinheiro, Cartão",
                       "restaurantImage": "imagem_restaurante.jpg",
                       "bankAccount": null
                     },
                     "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcGlfaWZvbWUiLCJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiZXhwIjoxNzI3MTk3NDMzLCJpYXQiOjE3MjcxOTcxMzN9.IhHau0U76i4wOyq5zwrfy16Af9jkvEejyfyIxG7yIyZDZRhrna9CyDj7QDNvTOpEcFezsXtVTUJtsAjDekrIUZSmTxfoDem_ynyab8Az_wM6Kpno3psamVHJstBtcWBTjcAOGNjqQbR-mGsC_1-0qnV79Yk4XA9rQj4giPRyx9kU6ey6z1HCbIpoqNMhAHCK7RVLto8bN7LMsz5nh00h0xsUng_pzB2WBlUhJ4yjcMPZcu9qPsOlFbeE79SmHAYMLZssLFmJNPBJuuFQPJoUXuMKQH4vFGOoLLYzBkleb0vN3wROVFD2EuHMxYQo2mE8K1MZm_LPCHotxU04pDmDOg"
                   },
                   "message": "Logado com sucesso"
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
