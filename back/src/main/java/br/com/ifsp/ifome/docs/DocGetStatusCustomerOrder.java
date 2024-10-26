package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Operation(
        summary = "Obtém o status do pedido em tempo real via SSE",
        description = """
        Este endpoint utiliza SSE (Server-Sent Events) para enviar atualizações de status de um pedido específico em tempo real.
        """,
        security = @SecurityRequirement(name = "Bearer Token"),
        responses = {
                @ApiResponse(responseCode = "200", description = "Status do pedido enviado com sucesso via SSE"),
                @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
        }
)


public @interface DocGetStatusCustomerOrder {
}
