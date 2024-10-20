package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.OrderItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@ApiResponses({
    @ApiResponse(
        responseCode = "201", description = "Atualiza um prato no carrinho",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(value = """
                {
                  "status": "success",
                  "data": {
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
                        "quantity": 2,
                        "unitPrice": 29.9,
                        "totalPrice": 29.9,
                        "restaurantId": 1,
                        "dishId": 3
                      }
                    ],
                    "totalprice": 59.8
                  },
                  "message": "Quantidade do prato atualizado com sucesso!"
                }
            """)
        )
    ),
    @ApiResponse(
        responseCode = "401", description = "Precisa estar autenticado"
    ),
    @ApiResponse(
        responseCode = "400", description = "Campo inválido",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(value = """
               {
                 "message": "Erro ao realizar operação",
                 "errors": {
                   "quantity": [
                     "O quantidade não pode ser 0 e deve conter apenas valores númericos e positivos"
                   ]
                 }
               }
            """)
        )
    )
})
@Operation(
    summary = "Atualizar prato no carrinho",
    security = @SecurityRequirement(name = "Bearer Token")
)
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
                name = "Atualização bem sucedido",
                description = "Atualizando item do pedido no carrinho",
                value = """
                  {
                   "dishId": 3,
                   "quantity": 1
                 }
                """
            ),
            @ExampleObject(
                name = "Atualizando item do pedido com prato Indisponível ou não presente no carrinho",
                description =
                    """
                        Prato que está com o status Indisponível ou não se encontra no carrinho
                        """,
                value = """
                    {
                       "dishId": 1,
                       "quantity": 1
                    }
                    """
            ),
            @ExampleObject(
                name = "Atualizando item do pedido com quantitdade inválida",
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
