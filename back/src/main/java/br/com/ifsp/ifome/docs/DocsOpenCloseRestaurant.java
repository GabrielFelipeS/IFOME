package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Alterar estado de aberto/fechado", security = @SecurityRequirement(name = "Bearer Token"))
@RequestBody(
    description = """
    Para realizar a abertura/fechamento do restaurante é necessario enviar o token jwt no header, com isso ele irá identificar o restaurante
    
    Não é necessario enviar nenhuma outra informação além do token
    """
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Restaurante aberto/fechado com sucesso!",
        content = @Content(mediaType = "application/json",
            examples = {
                @ExampleObject(name = "fechar restaurante", value = """
                {
                    "status": "success",
                    "message": "Restaurante fechado com sucesso!"
                }
            """),
                        @ExampleObject(name = "abrir restaurante", value = """
                {
                    "status": "success",
                    "message": "Restaurante aberto com sucesso!"
                }
            """)
            }
        )),
    @ApiResponse(responseCode = "401", description = "Não autenticado!",
        content = @Content(mediaType = "application/json"))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsOpenCloseRestaurant {
}
