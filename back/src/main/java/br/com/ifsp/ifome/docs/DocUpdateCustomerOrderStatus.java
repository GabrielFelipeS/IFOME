package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
        summary = "Atualiza o status de um pedido específico",
        description = "Este endpoint permite a atualização do status de um pedido, utilizando o ID do pedido.",
        security = @SecurityRequirement(name = "Bearer Token")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200", description = "Status atualizado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "status": "success",
                  "data": null,
                  "message": "Status atualizado com sucesso!"
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "400", description = "Erro de estado inválido",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "status": "error",
                  "data": null,
                  "message": "Estado inválido para esta operação."
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "404", description = "Pedido não encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "status": "error",
                  "data": null,
                  "message": "Pedido não encontrado."
                }
            """)
                )
        ),
        @ApiResponse(
                responseCode = "500", description = "Erro inesperado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = @ExampleObject(value = """
                {
                  "status": "error",
                  "data": null,
                  "message": "Erro inesperado: [detalhes do erro]"
                }
            """)
                )
        )
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocUpdateCustomerOrderStatus {
}
