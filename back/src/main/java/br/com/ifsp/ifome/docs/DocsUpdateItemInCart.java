package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Operation(
    summary = "Atualizar prato no carrinho",
    security = @SecurityRequirement(name = "Bearer Token"),
    responses = @ApiResponse(
        responseCode = "201", description = "Insere um prato no carrinho",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(value = """
                {
                  "status": "success",
                  "data": {
                    "client": {
                      "id": 2,
                      "name": "Gabriel",
                      "email": "email1@email.com",
                      "password": "$2a$10$rQP2X0ALCvxkpWkUnM/.o.JdpVtVSpQk5vurqg/otzk/motF9ObAG",
                      "dateOfBirth": "2003-04-14",
                      "cpf": "92051362041",
                      "address": [
                        {
                          "id": 2,
                          "nameAddress": "Endereço Maria",
                          "cep": "01234-567",
                          "neighborhood": "Centro",
                          "city": "São Paulo",
                          "state": "SP",
                          "address": "Avenida São João",
                          "number": "500",
                          "complement": "Apto 101",
                          "details": "Próximo ao Parque Villa-Lobos",
                          "typeResidence": "Apartamento"
                        }
                      ],
                      "role": "CLIENT",
                      "enabled": true,
                      "username": "email1@email.com",
                      "authorities": [
                        {
                          "authority": "CLIENT_DELETE"
                        },
                        {
                          "authority": "CLIENT_CREATE"
                        },
                        {
                          "authority": "CLIENT_READ"
                        },
                        {
                          "authority": "CLIENT_UPDATE"
                        },
                        {
                          "authority": "ROLE_CLIENT"
                        }
                      ],
                      "accountNonLocked": true,
                      "credentialsNonExpired": true,
                      "accountNonExpired": true
                    },
                    "orderItems": [
                      {
                        "id": 7,
                        "dish": {
                          "id": 3,
                          "name": "Hambúrguer Artesanal",
                          "description": "Hambúrguer com queijo, alface e tomate",
                          "price": 29.9,
                          "dishCategory": "Prato Principal",
                          "dishImage": "hamburguer.jpg",
                          "availability": "Disponível",
                          "restaurantId": 1
                        },
                        "quantity": 1,
                        "unitPrice": 29.9,
                        "totalPrice": 29.9,
                        "restaurantId": 1,
                        "dishId": 3
                      }
                    ],
                    "totalprice": 29.9
                  },
                  "message": "Prado adicionado no carrinho"
                }
            """)
    )
))
@RequestBody(description = """
    É necessário enviar um token com a role client (possivel ao logarm em /api/auth/client)
    Validações dos campos:
    
    ## dishId:
    
    Tipo: Long
    
    Validação:
   - Não aceita valores nulos
   - Não aceita números negativos
   - Não aceita zero
    
    Anotação: @Positive, @NotNull
    
   ## quantity:
    
   Tipo: Integer
    
   Validação:
   - Não aceita valores nulos
   - Não aceita números negativos
   - Não aceita zero
    
    Anotação: @Positive, @NotNull""",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = OrderItemRequest.class),
        examples = {
            @ExampleObject(
                name = "Inserção bem sucedido",
                description = "Inserindo item do pedido ao carrinho",
                value = """
                  {
                   "dishId": 3,
                   "quantity": 1
                 }
                """
            ),
            @ExampleObject(
                name = "Inserir item do pedido com prato Indisponível",
                description =
                    """
                        Prato que está com o status Indisponível
                        """,
                value = """
                    {
                       "dishId": 1,
                       "quantity": 1
                    }
                    """
            ),
            @ExampleObject(
                name = "Inserir item do pedido com quantitdade inválida",
                description =
                    """
                        Inserir item do pedido que esta com quantidae inválida
                        """,
                value = """
                    {
                       "dishId": 3,
                       "quantity": -1
                    }
                    """
            )

        }
    ))
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsUpdateItemInCart {
}
