package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
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
    summary = "Pegar carrinho",
    security = @SecurityRequirement(name = "Bearer Token"),
    responses = {
        @ApiResponse(responseCode = "200", description = "Retorna o carrinho do usuário",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(value = """
                 {
                           "status": "success",
                           "data": {
                             "orderItems": [],
                             "totalprice": 0
                           },
                           "message": "Carrinho encontrado!"
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
public @interface DocsGetCart {

}
