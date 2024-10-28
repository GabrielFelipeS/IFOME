package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Operation(
    summary = "Finalizar pedido com pratos do carrinho",
    security = @SecurityRequirement(name = "Bearer Token")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "201", description = "Finalizar pedido",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class),
            examples =  @ExampleObject(value = """
                {
                  "status": "success",
                  "data": {
                    "id": 2,
                    "orderPrice": 29.9,
                    "paymentStatus": "PENDENTE",
                    "orderDate": "2024-10-20T14:46:46.257836"
                  },
                  "message": "Pedido enviado! Aguarde confimação!"
                }
            """)
        )
    ),
    @ApiResponse(
        responseCode = "401", description = "Precisa estar autenticado"
    ),
    @ApiResponse(
        responseCode = "409", description = "Selecione pelo menos um prato",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(value = """
                {
                  "message": "Selecione pelo menos um prato para continuar."
                }
            """)
        )
    )
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateCustomerOrder {
}
