package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Operation(
        summary = "Retorna todos os pedidos do cliente autenticado",
        description = """
        Este endpoint retorna todos os pedidos associados ao cliente autenticado.
        """,
        security = @SecurityRequirement(name = "Bearer Token"),
        responses = {
                @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado para o cliente"),
                @ApiResponse(responseCode = "401", description = "Cliente não autenticado")
        }
)


public @interface DocGetCustomerOrders {
}
