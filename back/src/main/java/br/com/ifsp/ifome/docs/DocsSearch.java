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
        summary = "Pesquisar restaurantes ou pratos",
        description = "Pesquisa por nome de restaurantes ou pratos, retornando resultados baseados no termo fornecido.",
        responses = {
                @ApiResponse(responseCode = "200", description = "Resultados encontrados",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(value = """
                    {
                        "status": "success",
                        "data": {
                            "restaurants": [
                                {
                                    "id": 1,
                                    "nameRestaurant": "Açai do Monge",
                                    "cnpj": "58.911.612/0001-16",
                                    "foodCategory": "Sorveteria",
                                    "telephone": "(11) 1234-5678",
                                    "address": [],
                                    "openingHours": [],
                                    "personResponsible": "Nome Responsável",
                                    "email": "email1@email.com",
                                    "paymentMethods": "Dinheiro, Cartão"
                                }
                            ],
                            "dishes": [
                                {
                                    "id": 8,
                                    "name": "Bruschetta",
                                    "description": "Entrada com pão, tomate fresco e manjericão",
                                    "price": 12.9,
                                    "dishCategory": "Entrada",
                                    "dishImage": "bruschetta.jpeg",
                                    "availability": "AVAILABLE"
                                }
                            ]
                        },
                        "message": "Resultados encontrados."
                    }
                """)
                        )
                ),
                @ApiResponse(responseCode = "400", description = "Termo de pesquisa obrigatório",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(value = """
                    {
                        "message": "O termo de pesquisa é obrigatório."
                    }
                """)
                        )
                ),
                @ApiResponse(responseCode = "204", description = "Nenhum resultado encontrado",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(value = """
                    {
                        "message": "Nenhum resultado encontrado."
                    }
                """)
                        )
                )
        }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsSearch {
}

