package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.response.RestaurantReviewResponse;
import br.com.ifsp.ifome.dto.response.DishResponse;
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
        summary = "Obter restaurantes recomendados com base no histórico do usuário",
        description = "Este endpoint retorna uma lista de restaurantes recomendados para o usuário com base em suas preferências e histórico de pedidos.",
        security = @SecurityRequirement(name = "Bearer Token")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Restaurantes recomendados com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "name": "Restaurante A",
      "rating": 4.5,
      "cuisine": "Italiana",
      "address": "Rua X, 123",
      "image": "restauranteA.jpg"
    },
    {
      "id": 2,
      "name": "Restaurante B",
      "rating": 4.2,
      "cuisine": "Chinesa",
      "address": "Rua Y, 456",
      "image": "restauranteB.jpg"
    }
  ],
  "message": "Restaurantes recomendados com sucesso!"
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
                description = "Nenhum restaurante encontrado para recomendação",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
{
  "message": "Nenhum restaurante encontrado para recomendação."
}
"""))
        )
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsRecommendedRestaurants {
}
