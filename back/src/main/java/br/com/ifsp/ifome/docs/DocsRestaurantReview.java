package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.RestaurantReviewRequest;
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
@Operation(
        summary = "Avaliar restaurante após pedido entregue",
        description = "Permite que o cliente avalie o restaurante após a entrega do pedido, atribuindo uma nota de 1 a 5 estrelas e fornecendo um comentário opcional.",
        security = @SecurityRequirement(name = "Bearer Token")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200", description = "Avaliação registrada com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "status": "success",
                  "data": {
                    "id": 1,
                    "nameRestaurant": "Restaurante X",
                    "cnpj": "12345678000190",
                    "foodCategory": "Italiana",
                    "address": [...],
                    "telephone": "(11) 1234-5678",
                    "dish": [...],
                    "openingHours": [...],
                    "personResponsible": "João Silva",
                    "personResponsibleCPF": "123.456.789-00",
                    "email": "contato@restaurantex.com",
                    "paymentMethods": "Cartão, Dinheiro",
                    "restaurantImage": "/images/restaurant.jpg",
                    "bankAccount": {...},
                    "isOpen": true,
                    "rating": 4.5,
                    "stars": 5.0,
                    "comment": "Comida deliciosa!"
                  },
                  "message": "Avaliação registrada com sucesso!"
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "400", description = "Dados inválidos fornecidos ou Pedido já avaliado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "message": "Erro ao realizar operação",
                  "errors": {
                    "stars": [
                      "A nota deve ser entre 1 e 5"
                    ],
                    "comment": [
                      "O comentário deve ter no máximo 250 caracteres"
                    ]
                  }
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "401", description = "Usuário não autenticado"
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Avaliação não permitida (pedido não entregue ou não pertence ao cliente)",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = {
                                @ExampleObject(
                                        name = "Pedido não entregue",
                                        description = "O pedido não foi entregue, portanto não pode ser avaliado.",
                                        value = """
                    {
                      "message": "Apenas pedidos entregues podem ser avaliados."
                    }
                    """
                                ),
                                @ExampleObject(
                                        name = "Pedido não pertence ao cliente",
                                        description = "O pedido não pertence ao cliente autenticado.",
                                        value = """
                    {
                      "message": "Pedido não pertence ao cliente autenticado."
                    }
                    """
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "404", description = "Pedido não encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "message": "Pedido não encontrado."
                }
            """)))})
@RequestBody(description = """
    O cliente deve fornecer a nota entre 1 e 5 estrelas. O comentário é opcional, mas, se fornecido, deve ter no máximo 250 caracteres.
    Validações:
    
    ## stars:
    Tipo: Float
    Validação:
    - Não aceita valores nulos
    - Deve ser um valor entre 1 e 5
    
    ## comment:
    Tipo: String
    Validação:
    - Máximo de 250 caracteres
    - Não é obrigatório
""",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantReviewRequest.class),
                examples = {
                        @ExampleObject(
                                name = "Avaliação bem-sucedida",
                                description = "Cliente atribui uma nota e um comentário opcional",
                                value = """
                  {
                   "stars": 4.0,
                   "comment": "A comida estava excelente, mas o serviço demorou um pouco."
                 }
                """
                        ),
                        @ExampleObject(
                                name = "Avaliação com nota inválida",
                                description = "Cliente fornece uma nota fora do intervalo permitido",
                                value = """
                    {
                       "stars": 6.0,
                       "comment": "Muito bom!"
                    }
                    """
                        ),
                        @ExampleObject(
                                name = "Avaliação sem comentário",
                                description = "Cliente avalia o restaurante sem fornecer um comentário",
                                value = """
                    {
                       "stars": 5.0
                    }
                    """
                        )
                }
        ))
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsRestaurantReview {
}
