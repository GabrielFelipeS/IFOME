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

                                      "status": "sucess",
                                      "data": [
                                        {
                                          "id": 1,
                                          "nameRestaurant": "Açai do monge",
                                          "cnpj": "58911612000116",
                                          "foodCategory": "Sorveteria",
                                          "address": [
                                            {
                                              "id": 5,
                                              "nameAddress": "Endereço Carlos",
                                              "cep": "03090-000",
                                              "neighborhood": "Mooca",
                                              "city": "São Paulo",
                                              "state": "SP",
                                              "address": "Rua da Mooca",
                                              "number": "300",
                                              "complement": "Apto 202",
                                              "details": "Perto do Parque da Mooca",
                                              "typeResidence": "Casa",
                                              "latitude": "-23.5585905",
                                              "longitude": "-46.5900739"
                                            }
                                          ],
                                          "telephone": "(11) 1234-5678",
                                          "dish": [
                                            {
                                              "id": 1,
                                              "restaurantId": 1,
                                              "name": "Açai com Frutas",
                                              "description": "Açai com banana, morango e granola",
                                              "price": 19.9,
                                              "dishCategory": "Sobremesa",
                                              "dishImage": "acai_frutas.jpeg",
                                              "availability": "Indisponível"
                                            },
                                            {
                                              "id": 2,
                                              "restaurantId": 1,
                                              "name": "Pizza de Calabresa",
                                              "description": "Pizza com calabresa, cebola e azeitonas",
                                              "price": 34.9,
                                              "dishCategory": "Prato Principal",
                                              "dishImage": "pizza_calabresa.jpeg",
                                              "availability": "Indisponível"
                                            }
                                          ],
                                          "openingHours": [
                                            {
                                              "id": 1,
                                              "dayOfTheWeek": "Segunda-feira",
                                              "opening": "08:00",
                                              "closing": "22:00"
                                            },
                                            {
                                              "id": 7,
                                              "dayOfTheWeek": "Domingo",
                                              "opening": "09:00",
                                              "closing": "22:00"
                                            }
                                          ],
                                          "personResponsible": "Nome Responsável",
                                          "personResponsibleCPF": "07635915053",
                                          "email": "email1@email.com",
                                          "paymentMethods": "Dinheiro, Cartão",
                                          "restaurantImage": "monge.jpeg",
                                          "bankAccount": {
                                            "bank": "Banco do Brasil",
                                            "agency": "1234",
                                            "account": "00012345-6"
                                          },
                                          "isOpen": true,
                                          "restaurantReview": [
                                            {
                                              "id": 1,
                                              "customerOrder": {
                                                "orderId": 7,
                                                "name": "Gabriel",
                                                "address": {
                                                  "id": 1,
                                                  "nameAddress": "Endereço João",
                                                  "cep": "05413-020",
                                                  "neighborhood": "Pinheiros",
                                                  "city": "São Paulo",
                                                  "state": "SP",
                                                  "address": "Rua dos Três Irmãos",
                                                  "number": "50",
                                                  "complement": null,
                                                  "details": "Próximo à Praça da República",
                                                  "typeResidence": "Casa",
                                                  "latitude": "-23.5895527",
                                                  "longitude": "-46.7157754"
                                                },
                                                "orderItems": [
                                                  {
                                                    "id": 1,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  },
                                                  {
                                                    "id": 4,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  },
                                                  {
                                                    "id": 6,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  }
                                                ],
                                                "orderPrice": 1015,
                                                "orderInfo": [
                                                  {
                                                    "id": 11,
                                                    "orderStatus": "CONCLUIDO",
                                                    "localDateTime": "2024-12-04T17:25:19.848997"
                                                  }
                                                ],
                                                "paymentStatus": "CONCLUIDO",
                                                "orderDate": "2024-12-04 17:25:19.848997",
                                                "freight": 0,
                                                "totalPrice": 1015
                                              },
                                              "stars": 4.5,
                                              "comment": "Excelente experiência no restaurante!"
                                            },
                                            {
                                              "id": 2,
                                              "customerOrder": {
                                                "orderId": 8,
                                                "name": "Gabriel",
                                                "address": {
                                                  "id": 1,
                                                  "nameAddress": "Endereço João",
                                                  "cep": "05413-020",
                                                  "neighborhood": "Pinheiros",
                                                  "city": "São Paulo",
                                                  "state": "SP",
                                                  "address": "Rua dos Três Irmãos",
                                                  "number": "50",
                                                  "complement": null,
                                                  "details": "Próximo à Praça da República",
                                                  "typeResidence": "Casa",
                                                  "latitude": "-23.5895527",
                                                  "longitude": "-46.7157754"
                                                },
                                                "orderItems": [
                                                  {
                                                    "id": 1,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  },
                                                  {
                                                    "id": 6,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  }
                                                ],
                                                "orderPrice": 1015,
                                                "orderInfo": [
                                                  {
                                                    "id": 12,
                                                    "orderStatus": "CONCLUIDO",
                                                    "localDateTime": "2024-12-04T17:25:19.857373"
                                                  }
                                                ],
                                                "paymentStatus": "CONCLUIDO",
                                                "orderDate": "2024-12-04 17:25:19.857373",
                                                "freight": 0,
                                                "totalPrice": 1015
                                              },
                                              "stars": 4,
                                              "comment": "BOM!"
                                            },
                                            {
                                              "id": 3,
                                              "customerOrder": {
                                                "orderId": 6,
                                                "name": "Gabriel",
                                                "address": {
                                                  "id": 1,
                                                  "nameAddress": "Endereço João",
                                                  "cep": "05413-020",
                                                  "neighborhood": "Pinheiros",
                                                  "city": "São Paulo",
                                                  "state": "SP",
                                                  "address": "Rua dos Três Irmãos",
                                                  "number": "50",
                                                  "complement": null,
                                                  "details": "Próximo à Praça da República",
                                                  "typeResidence": "Casa",
                                                  "latitude": "-23.5895527",
                                                  "longitude": "-46.7157754"
                                                },
                                                "orderItems": [
                                                  {
                                                    "id": 1,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  },
                                                  {
                                                    "id": 2,
                                                    "dish": {
                                                      "id": 4,
                                                      "name": "Milkshake de Chocolate",
                                                      "description": "Milkshake cremoso de chocolate",
                                                      "price": 14.9,
                                                      "dishCategory": "Bebida",
                                                      "dishImage": "milkshake_chocolate.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 4
                                                  },
                                                  {
                                                    "id": 3,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  },
                                                  {
                                                    "id": 4,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  },
                                                  {
                                                    "id": 5,
                                                    "dish": {
                                                      "id": 4,
                                                      "name": "Milkshake de Chocolate",
                                                      "description": "Milkshake cremoso de chocolate",
                                                      "price": 14.9,
                                                      "dishCategory": "Bebida",
                                                      "dishImage": "milkshake_chocolate.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 4
                                                  },
                                                  {
                                                    "id": 6,
                                                    "dish": {
                                                      "id": 3,
                                                      "name": "Hambúrguer Artesanal",
                                                      "description": "Hambúrguer com queijo, alface e tomate",
                                                      "price": 29.9,
                                                      "dishCategory": "Prato Principal",
                                                      "dishImage": "hamburguer.jpeg",
                                                      "availability": "Disponível",
                                                      "restaurantId": 1
                                                    },
                                                    "quantity": 5,
                                                    "unitPrice": 29,
                                                    "detail": null,
                                                    "totalPrice": 145,
                                                    "restaurantId": 1,
                                                    "dishId": 3
                                                  }
                                                ],
                                                "orderPrice": 1015,
                                                "orderInfo": [
                                                  {
                                                    "id": 10,
                                                    "orderStatus": "CONCLUIDO",
                                                    "localDateTime": "2024-12-04T17:25:19.848997"
                                                  }
                                                ],
                                                "paymentStatus": "CONCLUIDO",
                                                "orderDate": "2024-12-04 17:25:19.848997",
                                                "freight": 0,
                                                "totalPrice": 1015
                                              },
                                              "stars": 4,
                                              "comment": "A comida estava excelente, mas o serviço demorou um pouco."
                                            }
                                          ],
                                          "rating": 4.166666666666667
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
