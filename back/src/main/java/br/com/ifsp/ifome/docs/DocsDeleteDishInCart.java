package br.com.ifsp.ifome.docs;

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
    summary = "Remover prato do carrinho",
    security = @SecurityRequirement(name = "Bearer Token")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "204", description = "Prato removido do carrinho"
    ),
    @ApiResponse(
        responseCode = "401", description = "Precisa estar autenticado"
    ),
    @ApiResponse(
        responseCode = "404", description = "Prato não encontrado no carrinho",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(value = """
                {
                  "message": "Prato não encontrado no carrinho"
                }
            """)
        )
    )
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsDeleteDishInCart {
}
