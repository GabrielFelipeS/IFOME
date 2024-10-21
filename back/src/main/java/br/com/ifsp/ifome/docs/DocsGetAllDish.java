package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
    summary = "Pegar pratos",
    responses = {
        @ApiResponse(responseCode = "200", description = "Pratos encontrados",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "status": "success",
                      "data": [
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
                      "message": "Busca por prato concluida"
                    }
                """)
            )
        )
    }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsGetAllDish {

}
