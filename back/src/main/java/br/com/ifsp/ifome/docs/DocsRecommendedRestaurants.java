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
                         },
                         {
                           "id": 3,
                           "restaurantId": 1,
                           "name": "Hambúrguer Artesanal",
                           "description": "Hambúrguer com queijo, alface e tomate",
                           "price": 29.9,
                           "dishCategory": "Prato Principal",
                           "dishImage": "hamburguer.jpeg",
                           "availability": "Disponível"
                         },
                         {
                           "id": 4,
                           "restaurantId": 1,
                           "name": "Milkshake de Chocolate",
                           "description": "Milkshake cremoso de chocolate",
                           "price": 14.9,
                           "dishCategory": "Bebida",
                           "dishImage": "milkshake_chocolate.jpeg",
                           "availability": "Disponível"
                         },
                         {
                           "id": 5,
                           "restaurantId": 1,
                           "name": "Salada Tropical",
                           "description": "Salada com frutas e molho de iogurte",
                           "price": 22.5,
                           "dishCategory": "Entrada",
                           "dishImage": "salada_tropical.jpeg",
                           "availability": "Disponível"
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
                           "id": 2,
                           "dayOfTheWeek": "Terça-feira",
                           "opening": "08:00",
                           "closing": "22:00"
                         },
                         {
                           "id": 3,
                           "dayOfTheWeek": "Quarta-feira",
                           "opening": "08:00",
                           "closing": "22:00"
                         },
                         {
                           "id": 4,
                           "dayOfTheWeek": "Quinta-feira",
                           "opening": "08:00",
                           "closing": "22:00"
                         },
                         {
                           "id": 5,
                           "dayOfTheWeek": "Sexta-feira",
                           "opening": "08:00",
                           "closing": "23:00"
                         },
                         {
                           "id": 6,
                           "dayOfTheWeek": "Sábado",
                           "opening": "09:00",
                           "closing": "23:00"
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
                       "rating": 4.166666666666667
                     }
                   ],
                   "message": "Restaurantes recomendados com sucesso."
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
