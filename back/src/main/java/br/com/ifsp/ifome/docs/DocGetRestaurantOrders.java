package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(
        summary = "Retorna todos os pedidos feitos para um restaurante",
        description = """
        Este endpoint retorna todos os pedidos feitos a um restaurante específico, baseado na autenticação do restaurante.
        """,
        security = @SecurityRequirement(name = "Bearer Token"),
        responses = {
                @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado para o restaurante"),
                @ApiResponse(responseCode = "401", description = "Restaurante não autenticado")
        }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocGetRestaurantOrders {
}
