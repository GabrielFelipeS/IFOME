package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
    summary = "Pegar restaurantes com paginação",
    parameters = {
        @Parameter(name = "page", description = "Número da página a ser retornada (0-indexed)", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", description = "Número de itens por página", required = false, schema = @Schema(type = "integer", defaultValue = "15")),
        @Parameter(name = "sort", description = "Critério de ordenação, no formato 'propriedade,direção' (por exemplo, 'nameRestaurant,asc' ou 'nameRestaurant,desc')", required = false, schema = @Schema(type = "string", defaultValue = "nameRestaurant,asc"))
    },
    requestBody = @RequestBody(required = false),
    responses = {
        @ApiResponse(responseCode = "200", description = "Restaurantes encontrados",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "status": "success",
                      "data": {
                        "content": [
                          {
                            "id": 8,
                            "name": "Bruschetta",
                            "description": "Entrada com pão, tomate fresco e manjericão",
                            "price": 12.9,
                            "dishCategory": "Entrada",
                            "dishImage": "bruschetta.jpeg",
                            "availability": "Disponível"
                          },
                          {
                            "id": 3,
                            "name": "Hambúrguer Artesanal",
                            "description": "Hambúrguer com queijo, alface e tomate",
                            "price": 29.9,
                            "dishCategory": "Prato Principal",
                            "dishImage": "hamburguer.jpeg",
                            "availability": "Disponível"
                          },
                          {
                            "id": 4,
                            "name": "Milkshake de Chocolate",
                            "description": "Milkshake cremoso de chocolate",
                            "price": 14.9,
                            "dishCategory": "Bebida",
                            "dishImage": "milkshake_chocolate.jpeg",
                            "availability": "Disponível"
                          },
                          {
                            "id": 6,
                            "name": "Pizza Margherita",
                            "description": "Pizza tradicional com molho de tomate, mussarela e manjericão",
                            "price": 32.9,
                            "dishCategory": "Prato Principal",
                            "dishImage": "pizza_margherita.jpeg",
                            "availability": "Disponível"
                          },
                          {
                            "id": 7,
                            "name": "Pizza Quatro Queijos",
                            "description": "Pizza com uma combinação de queijos: mussarela, gorgonzola, parmesão e provolone",
                            "price": 36.9,
                            "dishCategory": "Prato Principal",
                            "dishImage": "pizza_quatro_queijos.jpeg",
                            "availability": "Disponível"
                          },
                          {
                            "id": 5,
                            "name": "Salada Tropical",
                            "description": "Salada com frutas e molho de iogurte",
                            "price": 22.5,
                            "dishCategory": "Entrada",
                            "dishImage": "salada_tropical.jpeg",
                            "availability": "Disponível"
                          },
                          {
                            "id": 10,
                            "name": "Suco de Laranja",
                            "description": "Suco natural de laranja",
                            "price": 8.9,
                            "dishCategory": "Bebida",
                            "dishImage": "suco_laranja.jpeg",
                            "availability": "Disponível"
                          }
                        ],
                        "pageable": {
                          "pageNumber": 0,
                          "pageSize": 15,
                          "sort": {
                            "empty": false,
                            "sorted": true,
                            "unsorted": false
                          },
                          "offset": 0,
                          "paged": true,
                          "unpaged": false
                        },
                        "last": true,
                        "totalPages": 1,
                        "totalElements": 7,
                        "size": 15,
                        "number": 0,
                        "sort": {
                          "empty": false,
                          "sorted": true,
                          "unsorted": false
                        },
                        "first": true,
                        "numberOfElements": 7,
                        "empty": false
                      },
                      "message": "Busca por prato concluida"
                    }
                """)
            )
        )
    }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsGetPaginationDish {

}
