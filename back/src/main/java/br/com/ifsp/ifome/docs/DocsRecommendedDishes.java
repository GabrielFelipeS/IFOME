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
  "status": "success",
  "data": [
    {
      "id": 1,
      "name": "Spaghetti à Carbonara",
      "description": "Delicioso spaghetti com molho cremoso de ovos e pancetta",
      "price": 35.0,
      "restaurantId": 1,
      "image": "spaghetti.jpg"
    },
    {
      "id": 2,
      "name": "Sushi Variado",
      "description": "Seleção de sushi fresco com peixe de alta qualidade",
      "price": 45.0,
      "restaurantId": 2,
      "image": "sushi.jpg"
    }
  ],
  "message": "Pratos recomendados com sucesso!"
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
