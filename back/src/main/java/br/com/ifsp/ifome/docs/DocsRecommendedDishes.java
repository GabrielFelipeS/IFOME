package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
        summary = "Obter pratos recomendados com base no histórico do usuário",
        description = "Este endpoint retorna uma lista de pratos recomendados para o usuário com base em suas preferências e histórico de pedidos.",
        security = @SecurityRequirement(name = "Bearer Token")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Pratos recomendados com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
{
  {
                                   "status": "sucess",
                                   "data": [
                                     {
                                       "id": 6,
                                       "restaurantId": 2,
                                       "name": "Pizza Margherita",
                                       "description": "Pizza tradicional com molho de tomate, mussarela e manjericão",
                                       "price": 32.9,
                                       "dishCategory": "Prato Principal",
                                       "dishImage": "pizza_margherita.jpeg",
                                       "availability": "Disponível"
                                     },
                                     {
                                       "id": 7,
                                       "restaurantId": 2,
                                       "name": "Pizza Quatro Queijos",
                                       "description": "Pizza com uma combinação de queijos: mussarela, gorgonzola, parmesão e provolone",
                                       "price": 36.9,
                                       "dishCategory": "Prato Principal",
                                       "dishImage": "pizza_quatro_queijos.jpeg",
                                       "availability": "Disponível"
                                     },
                                     {
                                       "id": 10,
                                       "restaurantId": 2,
                                       "name": "Suco de Laranja",
                                       "description": "Suco natural de laranja",
                                       "price": 8.9,
                                       "dishCategory": "Bebida",
                                       "dishImage": "suco_laranja.jpeg",
                                       "availability": "Disponível"
                                     }
                                   ],
                                   "message": "Pratos Recomendados com sucesso"
                                 }
""")
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Usuário não autenticado"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Nenhum prato encontrado para recomendação",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
{
  "message": "Nenhum prato encontrado para recomendação."
}
""")
                )
        )
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsRecommendedDishes {
}
