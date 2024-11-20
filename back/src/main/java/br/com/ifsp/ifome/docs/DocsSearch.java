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
                                                    "id": 2,
                                                    "nameRestaurant": "Pizzaria do Chef",
                                                    "cnpj": "12345678000199",
                                                    "foodCategory": "Pizzaria",
                                                    "address": [
                                                      {
                                                        "id": 6,
                                                        "nameAddress": "Endereço Chef",
                                                        "cep": "22041-001",
                                                        "neighborhood": "Copacabana",
                                                        "city": "Rio de Janeiro",
                                                        "state": "RJ",
                                                        "address": "Avenida Atlântica",
                                                        "number": "1000",
                                                        "complement": "Loja 3",
                                                        "details": "Frente à praia",
                                                        "typeResidence": "Comercial",
                                                        "latitude": "-22.9716406",
                                                        "longitude": "-43.1845041"
                                                      }
                                                    ],
                                                    "telephone": "(21) 9876-5432",
                                                    "dish": [
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
                                                        "id": 8,
                                                        "name": "Bruschetta",
                                                        "description": "Entrada com pão, tomate fresco e manjericão",
                                                        "price": 12.9,
                                                        "dishCategory": "Entrada",
                                                        "dishImage": "bruschetta.jpeg",
                                                        "availability": "Disponível"
                                                      },
                                                      {
                                                        "id": 9,
                                                        "name": "Tiramisu",
                                                        "description": "Sobremesa italiana com café e mascarpone",
                                                        "price": 18.9,
                                                        "dishCategory": "Sobremesa",
                                                        "dishImage": "tiramisu.jpeg",
                                                        "availability": "Indisponível"
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
                                                    "openingHours": [
                                                      {
                                                        "id": 8,
                                                        "dayOfTheWeek": "Segunda-feira",
                                                        "opening": "18:00",
                                                        "closing": "23:00"
                                                      },
                                                      {
                                                        "id": 9,
                                                        "dayOfTheWeek": "Terça-feira",
                                                        "opening": "18:00",
                                                        "closing": "23:00"
                                                      },
                                                      {
                                                        "id": 10,
                                                        "dayOfTheWeek": "Quarta-feira",
                                                        "opening": "18:00",
                                                        "closing": "23:00"
                                                      },
                                                      {
                                                        "id": 11,
                                                        "dayOfTheWeek": "Quinta-feira",
                                                        "opening": "18:00",
                                                        "closing": "23:00"
                                                      },
                                                      {
                                                        "id": 12,
                                                        "dayOfTheWeek": "Sexta-feira",
                                                        "opening": "18:00",
                                                        "closing": "00:00"
                                                      },
                                                      {
                                                        "id": 13,
                                                        "dayOfTheWeek": "Sábado",
                                                        "opening": "18:00",
                                                        "closing": "00:00"
                                                      },
                                                      {
                                                        "id": 14,
                                                        "dayOfTheWeek": "Domingo",
                                                        "opening": "18:00",
                                                        "closing": "22:00"
                                                      }
                                                    ],
                                                    "personResponsible": "Chef Antônio",
                                                    "personResponsibleCPF": "12345678901",
                                                    "email": "email2@email.com",
                                                    "paymentMethods": "Dinheiro, Cartão, Pix",
                                                    "restaurantImage": "pizzaria_chef.jpeg",
                                                    "bankAccount": {
                                                      "bank": "Caixa Econômica",
                                                      "agency": "4321",
                                                      "account": "00123456-7"
                                                    },
                                                    "isOpen": true
                                                  }
                                                ],
                                                "dishes": [
                                                  {
                                                    "id": 2,
                                                    "name": "Pizza de Calabresa",
                                                    "description": "Pizza com calabresa, cebola e azeitonas",
                                                    "price": 34.9,
                                                    "dishCategory": "Prato Principal",
                                                    "dishImage": "pizza_calabresa.jpeg",
                                                    "availability": "Indisponível"
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
                @ApiResponse(responseCode = "204", description = "Nenhum resultado encontrado")
        }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsSearch {
}

