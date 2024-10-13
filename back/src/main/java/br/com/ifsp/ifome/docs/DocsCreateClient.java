package br.com.ifsp.ifome.docs;

import br.com.ifsp.ifome.dto.request.ClientRequest;
import br.com.ifsp.ifome.dto.response.ClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.validator.constraints.br.CPF;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Cadastrar um novo cliente")
@RequestBody(
    description = """
        # Detalhes do cliente a ser criado
        Campos obrigatórios: Nome completo, E-mail, Telefone, Senha, Confirmação de senha, Data de nascimento, CPF, Endereços
        
        Validações dos campos:
        
        ## name:
        
        Tipo: String
        
        Validação: Não aceita valores nulos, vazios ou contendo apenas espaços.
        
        Anotação: @NotBlank
        
        ## email:
        
        Tipo: String
        
        Validações:
        
        - Validação sintática do e-mail.
       
        - Não aceita e-mails repetidos no sistema.
        
        - Não aceita valores nulos, vazios ou contendo apenas espaços.
        
        Anotações: @Email, @NotBlank, @NotRegisteredEmail
        
        ## password:
       
        Tipo: String
        
        Validações:
        
        - Senha precisa conter pelo menos uma letra minúscula.
        
        - Senha precisa conter pelo menos uma letra maiúscula.
        
        - Senha precisa conter pelo menos um número.
        
        - Senha precisa conter pelo menos um caractere especial.
        
        - Senha precisa possui pelo menos 6 caracteres.
        
        - Não pode conter espaços.
        
        - Não aceita valores nulos ou vazios.
        
        Anotações: @ValidPassword, @NotBlank
        
        ## confirmationPassword:
        
        Tipo: String
        
        Validações:
        
        - Deve ser igual ao campo password.
        
        - Não aceita valores nulos ou vazios.
        
        Anotações: @NotBlank, @ConfirmationPasswordEqualsPassword
        
        ## dateOfBirth:
        
        Tipo: LocalDate
        
        Validações:
        
        - A data deve estar no passado.
        
        - A idade mínima deve ser de 13 anos para cadastro no sistema.
        
        - Não aceita valores nulos.
        
        Anotações: @Past, @MinAgeToUse(minAge = 14), @NotNull
        
        ## cpf:
        
        Tipo: String
        
        Validações:
        
        - CPF válido com verificação sintática e com o algoritmo de Módulo 11.
        
        - Não aceita valores nulos ou vazios.
        
        - Não aceita CPF repetidos
        
        Anotações: @CPF, @NotBlank
        
        ## phone:
        
        Tipo: String
        
        Validações:
        
        - Formato de telefone válido: (11) 2000-0000 ou (11) 90000-0000
        
        - regex: ^\\([1-9]{2}\\) (?:[2-8]|9[0-9])[0-9]{3}-[0-9]{4}$.
        
        - Não aceita valores nulos ou vazios.
       
        Anotações: @Pattern, @NotBlank
        
        ## address:
        
        Tipo: List<@Valid AddressRequest>
        
        Validações:
        
        - A lista não pode estar vazia.
        
        - Lista de endereços, onde cada item é validado individualmente.
        
        Anotação: @NotEmpty
        
        # Detalhes do AddressRequest
        
        Campos obrigatórios: CEP, Nome do Endereço, Bairro, Cidade, Estado, Endereço, Complemento, Número, Tipo de Residência.
        
        Validações dos campos:
        
        ## cep:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        - Validação do formato do CEP (utiliza a anotação @CEP).
        
        Anotações: @NotBlank, @CEP
        
        ## nameAddress:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        Anotação: @NotBlank
        
        ## neighborhood:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        Anotação: @NotBlank
        
        ## city:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        Anotação: @NotBlank
        
        ## state:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        Anotação: @NotBlank
        
        ## address:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        Anotação: @NotBlank
        
        ## complement:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        Anotação: @NotBlank
        
        ## number:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        - Deve ser um número (um ou mais dígitos).
        
        Anotações: @NotBlank, @Pattern(regexp = "\\\\d{1,}")
        
        ## typeResidence:
        
        Tipo: String
        
        Validações:
        
        - Não aceita valores nulos ou vazios.
        
        - Deve ser um dos seguintes valores: "casa", "apartamento", "condomínio".
        
        Anotações: @NotBlank, @Pattern(regexp = "(casa|apartamento|condom[ií]nio)")
        
        ## details:
        
        Tipo: String
        
        Validações:
        
        - Não obrigatório.
        
        Anotação: (Nenhuma validação)""",
    required = true,
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ClientRequest.class),
        examples = {
            @ExampleObject(
            name = "cliente válido",
            description = "Inserindo cliente válido",
            value = """
                    {
                      "name": "Gabriel",
                      "email": "novo@cliente.com",
                      "password": "Abc@1234",
                      "confirmationPassword": "Abc@1234",
                      "dateOfBirth": "2000-01-01",
                      "cpf": "211.039.180-44",
                      "phone": "(11) 99248-1491",
                      "address": {
                          "nameAddress": "casa principal",
                          "cep": "35170-222",
                          "neighborhood": "neighborhood",
                          "city": "Coronel Fabriciano",
                          "state": "Minas Gerais",
                          "address": "address",
                          "complement": "complement",
                          "number": "12",
                          "details": "details",
                          "typeResidence": "casa"
                        }
                    }
                """
        ),
            @ExampleObject(
                name = "cliente inválido",
                description =
                """
                    Inserindo cliente inválido
                    cpf: falta 1 número
                    
                    email: não possui @
                    
                    password: não possui letra maiúscula, número e caractere especial
                    
                    address: esta vazio""",
                value = """
                    {
                        "name": "Gabriel",
                        "cpf": "4860867801",
                        "email" : "gabgmail.com",
                        "password": "password",
                        "confirmationPassword": "password",
                        "dateOfBirth": "2003-04-14",
                        "address": {}
                    }
                    """
            )

        }
    )
)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
        examples =  @ExampleObject(value = """
                {
                    "status": "success",
                    "data": {
                         "id": 2,
                         "email": "novo@cliente.com",
                         "dateOfBirth": "2000-01-01",
                         "cpf": "211.039.180-44",
                         "address": [
                            {
                                 "cep": "35170-222",
                                 "neighborhood": "neighborhood",
                                 "city": "Coronel Fabriciano",
                                 "state": "Minas Gerais",
                                 "address": "address",
                                 "number": "12",
                                 "complement": "complement"
                             }
                         ]
                    },
                    "message": "Cliente criado com sucesso"
                }
            """)
      )),
    @ApiResponse(responseCode = "400", description = "Invalid request",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ClientResponse.class),
            examples = @ExampleObject(value = """
            {
                "password": [
                    "Senha precisa conter pelo menos um caractere especial",
                    "Senha precisa conter pelo menos um número",
                    "Senha precisa conter pelo menos um caractere maiúsculo"
                ],
                "address": [
                    "É necessário ter um endereço"
                ],
                "phone": [
                    "O campo \\"Telefone\\" é obrigatório"
                ],
                "cpf": [
                   "O campo \\"CPF\\" deve estar no formato: XXX.XXX.XXX-XX}"
                ],
                "email": [
                    "E-mail deve estar no formato: nome@dominio.co"
                ],
                 "clientRequest": [
                     "Senha e confirmar senha não são iguais"
                 ]
            }
            """)))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateClient { }
