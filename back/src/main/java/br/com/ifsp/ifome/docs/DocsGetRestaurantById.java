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
    summary = "Pegar restaurante por id",
    security = @SecurityRequirement(name = "Bearer Token"),
    responses = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(value = """
                    {
                        "status": "success",
                        "data": {
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
                        },
                        "message": "Restaurante encontrado com sucesso"
                    }
                """)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                        "message": "Restaurante não encontrado"
                    }
                """)
            )
        )
    }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsGetRestaurantById {

}
