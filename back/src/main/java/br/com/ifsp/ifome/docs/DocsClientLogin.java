package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Realizar login")
@RequestBody(
    description = """
        # Detalhes para login
        Campos obrigatórios: E-mail e Senha
      
        Validações dos campos:
      
        ## email:
      
        Tipo: String
      
        Validações:
      
        - Validação sintática do e-mail.
      
        - Não aceita valores nulos, vazios ou contendo apenas espaços.
      
        Anotações: @Email, @NotBlank
      
        ## password:
      
        Tipo: String
      
        Validações:
      
        - Não aceita valores nulos ou vazios.
      
        Anotações: @NotBlank
      
      """,
    required = true,
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ClientRequest.class),
        examples = {
            @ExampleObject(
            name = "login válido",
            description = "Locado com cliente válido",
            value = """
                    {
                      "email" : "email1@email.com",
                      "password": "@Password1"
                    }
                """
        ),
            @ExampleObject(
                name = "login inválido",
                description =
                """
                    password: senha incorreta
                    """,
                value = """
                    {
                        "email" : "email1@email.com",
                        "password": "senha_incorreta"
                    }
                    """
            )

        }
    )
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Cliente logado com sucesso",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
        examples =  @ExampleObject(value = """
            {
              "status": "success",
              "data": {
                "user": {
                  "id": 2,
                  "name": "Gabriel",
                  "email": "email1@email.com",
                  "dateOfBirth": "2003-04-14",
                  "cpf": "92051362041",
                  "address": [
                    {
                      "id": 2,
                      "nameAddress": "Endereço Maria",
                      "cep": "01234-567",
                      "neighborhood": "Centro",
                      "city": "São Paulo",
                      "state": "SP",
                      "address": "Avenida São João",
                      "number": "500",
                      "complement": "Apto 101', 'Próximo ao Parque Villa-Lobos",
                      "details": "Próximo à Praça da República",
                      "typeResidence": "Apartamento"
                    }
                  ]
                },
                "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcGlfaWZvbWUiLCJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiZXhwIjoxNzM0MDk4Mjc3LCJpYXQiOjE3Mjg4Mzg2NzcsImF1dGhvcml0aWVzIjpbIkNMSUVOVF9SRUFEIiwiQ0xJRU5UX0NSRUFURSIsIkNMSUVOVF9VUERBVEUiLCJDTElFTlRfREVMRVRFIiwiUk9MRV9DTElFTlQiXX0.cwQU5IK1rva8gq_xRHdmU-Zq-4AijNdVsI8k6sNcWxgNuQFTPH3Yi4B9OsyCsVlG_YwI5APlmY0yGK3DfMf3kFLtk5sIYqfEtR1UBynS5LQvvfyTlvp96Ih2E-nikc6MZD9TmSq5DL8rzCQL47dauOeAlSdil1ghgYrKcWhrcb0HBZe-9YQSDp0ej0hGmsW5db4EuVglIPFINsa7lEVu7AgowEHBMPKPSRx91XVYyfwThJbocVUNS6oo7DJotlZBBIlzmFMNW84V1PUfZEe_fSLGzJ1JgzVsT26w_ehEdz6VCA3zb6nHzxKU_t-ikIobUuEOtXa9ZAne85kRwfDsCg"
              },
              "message": "Cliente logado com sucesso"
            }
            """)
      )),
    @ApiResponse(responseCode = "403", description = "Email ou senha incorretos!",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = """
                {
                     "message": "email ou senha incorretos!"
                 }
            """)))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsClientLogin { }
