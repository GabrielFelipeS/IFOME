package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.response.CartResponse;
import br.com.ifsp.ifome.dto.response.ChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Operation(
    summary = "Busca um chat pelo ID",
    description = """
        Retorna o chat de uma pedido com base no ID fornecido.
        
        As entidades da URL demonstram o acesso e comunicação do chat, exemplo: /api/entidade1/entidade2/{customerOrderId}, é um chat que tanto a entidade1 quanto entidade2 podem pegar. 
        
        O acesso ao chat do pedido é restrido apenas para entidades envolvidas com o pedido
        """,
    security = @SecurityRequirement(name = "Bearer Token"),
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Chat encontrado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Você não possui permissão para acessar o endpoint"),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar o chat!")
    }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsGetChat {
}
