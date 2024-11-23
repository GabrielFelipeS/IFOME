package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.response.MessageResponse;
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
    summary = "Envia mensagem para um chat",
    description = """ 
        Insere uma mensagem em um o chat de uma pedido com base no ID fornecido. 
        
        As entidades da URL demonstram o acesso e comunicação do chat, exemplo: /api/entidade1/entidade2/{customerOrderId}, é um chat que tanto a entidade1 quanto entidade2 podem inserir mensagens. 
        
        O acesso ao chat do pedido é restrido apenas para entidades envolvidas com o pedido
        """,
    security = @SecurityRequirement(name = "Bearer Token"),
    responses = {
        @ApiResponse(
            responseCode = "204",
            description = "Mensagem inserida com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Você não possui permissão para acessar o endpoint"),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar o chat!")
    }
)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsPostChat {
}
