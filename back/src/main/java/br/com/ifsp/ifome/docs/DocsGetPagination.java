package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
                                         "id": 1,
                                         "nameRestaurant": "Açai do monge",
                                         "cnpj": "58.911.612/0001-16",
                                         "foodCategory": "Sorveteria",
                                         "address": [],
                                         "telephone": "(11) 1234-5678",
                                         "openingHours": [],
                                         "personResponsible": "Nome Responsável",
                                         "personResponsibleCPF": null,
                                         "email": "email1@email.com",
                                         "paymentMethods": "Dinheiro, Cartão",
                                         "restaurantImage": "image.jpg",
                                         "bankAccount": null,
                                         "isOpen": false
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
                                 "totalElements": 1,
                                 "totalPages": 1,
                                 "size": 15,
                                 "number": 0,
                                 "sort": {
                                     "empty": false,
                                     "sorted": true,
                                     "unsorted": false
                                 },
                                 "first": true,
                                 "numberOfElements": 1,
                                 "empty": false
                             },
                             "message": "Sucesso na busca dos restaurantes"
                         }
                """)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Não autenticado!",
            content = @Content(mediaType = "application/json"))
    }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsGetPagination {

}
