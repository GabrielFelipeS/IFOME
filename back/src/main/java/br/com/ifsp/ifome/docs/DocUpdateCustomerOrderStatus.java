package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.UpdateOrderStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Operation(
        summary = "Atualiza o status de um pedido",
        description = """
        Este endpoint atualiza o status de um pedido existente. Somente o restaurante associado ao pedido pode atualizar o status.
        """,
        security = @SecurityRequirement(name = "Bearer Token"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Requisição contendo o ID do pedido e o novo status",
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UpdateOrderStatusRequest.class),
                        examples = @ExampleObject(
                                value = """
                    {
                        "customerOrderId": 1,
                        "newStatus": "EM_PREPARO"
                    }
                """
                        )
                )
        ),
        responses = {
                @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(
                                        value = """
                        {
                            "status": "success",
                            "message": "Status atualizado com sucesso!"
                        }
                    """)
                        )),
                @ApiResponse(responseCode = "400", description = "Status inválido ou fora de sequência",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(
                                        value = """
                        {
                            "status": "error",
                            "message": "Status fora de sequência."
                        }
                    """)
                        )),
                @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
        }
)


public @interface DocUpdateCustomerOrderStatus {
}
