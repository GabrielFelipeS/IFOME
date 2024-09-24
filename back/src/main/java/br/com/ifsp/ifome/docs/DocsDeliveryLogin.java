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
            description = "Logando com entregador válido",
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
    @ApiResponse(responseCode = "200", description = "Entregador logado com sucesso",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
        examples =  @ExampleObject(value = """
                {
                    "status": "sucess",
                    "data": {
                    "user": {
                      "id": 1,
                      "name": "João da Silva",
                      "cpf": "528.003.140-28",
                      "email": "email1@email.com",
                      "dateOfBirth": "1985-06-15",
                      "typeOfVehicle": "carro",
                      "plate": "ABC-1234",
                      "telephone": "(11) 98765-4321",
                      "cnhNumber": "123456789",
                      "cnhValidity": "2026-12-31",
                      "vehicleDocument": "987654321",
                      "address": [],
                     "bankAccount": {
                        "bank": "123",
                        "agency": "1255",
                        "account": "4547-7"
                      }
                    },
                        "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcGlfaWZvbWUiLCJzdWIiOiJlbWFpbDFAZ21haWwuY29tIiwiZXhwIjoxNzI3MTg3MDY2LCJpYXQiOjE3MjcxODY3NjZ9.mxpDmAveBrq6SGOELPfthhHobx9DIiHTWE7Yigv_yBNfei1atWXxsQnlhhTrpM3IzE8-hTrY_BtPTvAWHR3pgq0N_ZC2g8DnJz5LfE1rfsAqiWDur_xgHnApD6O08fN-8wvVo9kUvy-taEN5cvoV-s7TIeNTu-lvcR1St8MFwr9NeXtcFRy3O09FFRB2AgLcP52_-IjpK_2jhmi_10dBDGd6HAL3P_e6esG71Qn6fOHnDz2E7340T5JU3ngg3Iij176jR7EYg-yRw1wEf6Ijw1r04sOXkWhPjmSWtH18IVFS7Zx4o56ojjta5q5J5Se99DgPLXRKffnptkzJUchpOw"
                    },
                    "message": "Entregador logado com sucesso"
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
public @interface DocsDeliveryLogin { }
