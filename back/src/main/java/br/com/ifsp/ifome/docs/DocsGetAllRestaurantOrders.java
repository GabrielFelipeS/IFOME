package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
        summary = "Listar pedidos do restaurante",
        security = @SecurityRequirement(name = "Bearer Token"),

description = "Este endpoint retorna todos os pedidos associados ao restaurante do usuário autenticado, incluindo informações detalhadas de cada pedido, itens, endereço e status de pagamento.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Pedidos do restaurante retornados com sucesso",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiResponse.class),
                                examples = @ExampleObject(value = """
                    {
                      "status": "success",
                      "data": [
                        {
                          "orderId": 1,
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
                            "typeResidence": "Casa"
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
                              "dishId": 3,
                              "restaurantId": 1,
                              "totalPrice": 145
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
                              "unitPrice": 14.9,
                              "detail": null,
                              "dishId": 4,
                              "restaurantId": 1,
                              "totalPrice": 74.5
                            }
                          ],
                          "orderPrice": 219.5,
                          "orderInfo": [
                            {
                              "id": 1,
                              "orderStatus": "NOVO",
                              "localDateTime": "2024-10-28T19:36:18.350529"
                            }
                          ],
                          "paymentStatus": "PENDENTE",
                          "orderDate": "2024-10-28T19:36:18.350529"
                        }
                      ],
                      "message": "Lista de pedidos recuperada com sucesso"
                    }
                """)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Pedidos não encontrados para o restaurante autenticado"
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Não autorizado, autenticação necessária"
                )
        }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsGetAllRestaurantOrders {
}
