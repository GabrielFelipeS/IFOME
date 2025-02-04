package br.com.ifsp.ifome.docs;


import br.com.ifsp.ifome.dto.request.DeliveryPersonRequest;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
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

@Operation(summary = "Cadastrar um novo Entregador")
@RequestBody(
        description = """
            Detalhes do entregador a ser criado
            Campos obrigatórios: Nome, CPF, E-mail, Telefone, Senha, Confirmação de senha, Data de nascimento, Tipo de veículo, Placa, Número da CNH, Validade da CNH, RENAVAM, Endereços, Conta bancária

            Validações dos campos:
           
           ## name:
           
            Tipo: String
           
            Validações:
           
            Não aceita valores nulos, vazios ou contendo apenas espaços.
           
            Anotações: @NotBlank(message = "Nome é obrigatório")
           
           ## cpf:
           
            Tipo: String
           
            Validações:
           
            CPF válido (utilizando o algoritmo de Módulo 11).
           
            Não aceita valores nulos, vazios ou contendo apenas espaços.
           
            Anotações: @CPF(message = "CPF inválido"), @NotBlank(message = "CPF é obrigatório")
           
           ## email:
           
            Tipo: String
           
            Validações:
           
            Validação sintática do e-mail.
           
            Não aceita e-mails repetidos no sistema.
           
            Não aceita valores nulos, vazios ou contendo apenas espaços.
           
            Anotações: @Email(message = "E-mail inválido"), @NotBlank(message = "E-mail é obrigatório"), @NotRegisteredEmailDeliveryPerson
           
           ## password:
           
            Tipo: String
           
            Validações:
           
            Deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número, um caractere especial.
           
            Deve ter pelo menos 6 caracteres.
           
            Não pode conter espaços.
           
            Não aceita valores nulos ou vazios.
           
            Anotações: @ValidPassword, @NotBlank(message = "Senha é obrigatório")
           
           ## confirmationPassword:
           
            Tipo: String
           
            Validações:
           
            Deve ser igual ao campo password.
           
            Não aceita valores nulos ou vazios.
           
            Anotações: @NotBlank(message = "Confirmação de senha é obrigatório"), @ConfirmationPasswordEqualsPassword(message = "As senhas não coincidem")
           
           ## dateOfBirth:
           
            Tipo: String (convertida para LocalDate no método convertDateOfBirth)
           
            Validações:
           
            Deve estar no passado.
           
            Idade mínima deve ser de 14 anos.
           
            Não aceita valores nulos.
           
            Deve estar no formato correto de data.
           
            Anotações: @Past(message = "Data de nascimento deve estar no passado"), @MinAgeToUse(minAge = 14), @NotNull(message = "Data de nascimento é obrigatório"), @DateFormat(message = "Formato incorreto para data de nascimento")
           
           ## typeOfVehicle:
           
            Tipo: String
           
            Validações:
           
            Deve ser "carro" ou "moto".
           
            Anotações: @Pattern(regexp = "^([Cc]arro|[Mm]oto)$", message = "Tipo do veículo inválido")
           
           ## plate:
           
            Tipo: String
           
            Validações:
           
            Deve estar no formato XXX-9999.
           
            Não aceita valores nulos ou vazios.
           
            Anotações: @NotBlank(message = "Verifique a placa"), @Pattern(regexp = "[A-Z]{3}-\\\\d{4}", message = "A placa deve estar no formato XXX-9999")
           
           ## telephone:
           
            Tipo: String
           
            Validações:
           
            Deve estar no formato (XX) XXXXX-XXXX.
           
            Não aceita valores nulos ou vazios.
           
            Anotações: @NotBlank(message = "Telefone é obrigatório"), @Pattern(regexp = "\\\\(\\\\d{2}\\\\) \\\\d{4,5}-\\\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
           
           ## cnhNumber:
           
            Tipo: String
           
            Validações:
           
            O número da CNH deve conter entre 9 e 11 dígitos numéricos.
           
            Não aceita valores nulos ou vazios.
           
            Anotações: @NotBlank(message = "Número do CNH é obrigatório"), @Pattern(regexp = "\\\\d{9}", message = "O número da CNH deve conter exatamente 9 dígitos numéricos")
           
           ## cnhValidity:
           
            Tipo: LocalDate
           
            Validações:
           
            Deve estar no futuro.
           
            Não aceita valores nulos.
           
            Anotações: @Future(message = "CNH fora de validade"), @NotNull(message = "Validade da CNH é obrigatória")
           
           ## vehicleDocument:
           
            Tipo: String
           
            Validações:
           
            Deve conter entre 9 e 11 dígitos numéricos.
           
            Não aceita valores nulos ou vazios.
           
            Anotações: @NotBlank(message = "RENAVAM é obrigatório"), @Pattern(regexp = "\\\\d{9,11}", message = "O RENAVAM deve conter entre 9 e 11 dígitos numéricos")
           
           ## address:
           
            Tipo: List<@Valid AddressRequest>
           
            Validações:
           
            A lista não pode estar vazia.
           
            Cada item é validado individualmente.
           
            Anotações: @NotEmpty(message = "É necessário ter pelo menos um endereço")
           
            ## bankAccount:
        
            Tipo: BankAccountRequest
           
            Validações:
           
            - A lista não pode estar vazia.
           
            - Lista de endereços, onde cada item é validado individualmente.
           
            Anotação: @NotEmpty
           
           ## bank:
           
            Tipo: String
           
            Validações:
           
            Não aceita valores nulos, vazios ou contendo apenas espaços.
           
            Anotação: @NotBlank(message = "Banco é obrigatório")
           
           ## agency:
           
            Tipo: String
           
            Validações:
           
            Não aceita valores nulos, vazios ou contendo apenas espaços.
           
            Anotação: @NotBlank(message = "Agência é obrigatório")
           
           ## account:
           
            Tipo: String
           
            Validações:
           
            Não aceita valores nulos, vazios ou contendo apenas espaços.
           
            Anotação: @NotBlank(message = "Conta é obrigatório")
           """,
        required = true,
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DeliveryPersonRequest.class),
                examples = {
                        @ExampleObject(
                                name = "Entregador válido",
                                description = "Inserindo entregador válido",
                                value = """
                    {
                      "name": "Nome entregador",
                      "cpf": "033.197.356-16",
                      "email": "email@email.com",
                      "password": "@Senha1",
                      "confirmationPassword": "@Senha1",
                      "dateOfBirth": "1999-02-01",
                      "typeOfVehicle": "Carro",
                      "plate": "DIT-4987",
                      "telephone" : "(11) 1234-5678",
                      "cnhNumber" : "999999999",
                      "cnhValidity": "2028-01-02",
                      "vehicleDocument" : "111456789",
                      "address": {
                          "nameAddress": "casa principal",
                          "cep": "35170-222",
                          "neighborhood": "neighborhood",
                          "city": "Coronel Fabriciano",
                          "state": "Minas Gerais",
                          "address": "address",
                          "complement": "complement",
                          "number": "12",
                          "typeResidence" : "casa",
                          "details": "details"
                        },
                      "bankAccount" : {
                              "bank" : "123",
                              "agency" : "1255",
                              "account" : "4547-7"
                           }
                    }
                """
                        ),
                        @ExampleObject(
                                name = "entregador inválido",
                                description =
                                        """
                                            Inserindo entregador inválido
                                            nome: esta vazio
                                            dataOfBirth : esta vazio
                                            typeOfVehicle : esta vazio
                                            cpf: fora do padrão
                                            email: não possui @
                                            password: não possui letra maiúscula, número e caractere especial
                                            address: esta vazio
                                            telephone : telefone fora do padrão
                                            bankAccount: esta vazio
                                            plate:  Verique a placa
                                            cnhNumber : O número da CNH deve conter entre 9 e 11 dígitos numéricos
                                            cnhValidity": CNH com data inválida. Verifique a válidade
                                            vehicleDocument : O renavam deve conter de 9 à 11 digitos numéricos
                                     
                                        """,
                                value = """
                    {
                        "nome": "",
                        "dateOfBirth" : "",
                        "typeOfVehicle" : "",
                        "cpf": "4860867801",
                        "email" : "testeemail.com",
                        "password": "password",
                        "confirmationPassword": "password",
                        "bankAccount": {},
                        "address": {},
                        "telephone" : "12345678",
                        "plate": "DIT4987",
                        "cnhNumber" : "AE54785",
                        "cnhValidity": "2021-02-01",
                        "vehicleDocument" : "1545A"
                    }
                    """
                        )

                }
        )
)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Entregador criado com sucesso",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
            examples =  @ExampleObject(value = """
               {
                     "status": "success",
                     "data": {
                       "id": 2,
                       "name": "Nome entregador",
                       "cpf": "03319735616",
                       "email": "email@email.com",
                       "dateOfBirth": "1999-02-01",
                       "typeOfVehicle": "111456789",
                       "plate": "DIT-4987",
                       "telephone": "(11) 1234-5678",
                       "cnhNumber": "999999999",
                       "cnhValidity": "2028-01-02",
                       "vehicleDocument": "111456789",
                       "address": [
                         {
                           "id": 6,
                           "nameAddress": "casa principal",
                           "cep": "35170-222",
                           "neighborhood": "neighborhood",
                           "city": "Coronel Fabriciano",
                           "state": "Minas Gerais",
                           "address": "address",
                           "number": "12",
                           "complement": "complement",
                           "details": "details",
                           "typeResidence": "casa"
                         }
                       ],
                       "bankAccount": {
                         "bank": "123",
                         "agency": "1255",
                         "account": "4547-7"
                       }
                     },
                     "message": "Entragador cadastrado com sucesso"
                   }
            """)
        )),
    @ApiResponse(responseCode = "400", description = "Invalid request",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RestaurantResponse.class),
            examples = @ExampleObject(
                value = """
                    {
                       "message": "Erro ao realizar operação",
                       "errors": {
                         "address.typeResidence": [
                           "O campo \\"Tipo de residência\\" é obrigatório"
                         ],
                         "address.cep": [
                           "CEP inválido",
                           "O campo \\"Cep\\" é obrigatório"
                         ],
                         "address.city": [
                           "O campo \\"Cidade\\" é obrigatório"
                         ],
                         "address.state": [
                           "O campo \\"Estado\\" é obrigatório"
                         ],
                         "address.number": [
                           "O campo \\"Número\\" é obrigatório"
                         ],
                         "address.neighborhood": [
                           "O campo \\"Bairro\\" é obrigatório"
                         ],
                         "dateOfBirth": [
                           "Formato incorreto para data de nascimento",
                           "Para cadastro no sistema, é necessário ter pelo menos 18 anos de idade.",
                           "Para cadastro no sistema, é necessário ter no máximo 90 anos de idade.",
                           "Data de nascimento deve estar no passado"
                         ],
                         "telephone": [
                           "Telefone deve estar no formato (XX) XXXXX-XXXX"
                         ],
                         "plate": [
                           "A placa deve estar no formato XXX-9999"
                         ],
                         "typeOfVehicle": [
                           "Tipo do veículo inválido, tipos de veiculos aceitos: carro e moto"
                         ],
                         "address.complement": [
                           "O campo \\"Complemento\\" é obrigatório"
                         ],
                         "address.address": [
                           "O campo \\"Address\\" é obrigatório"
                         ],
                         "bankAccount.bank": [
                           "O campo \\"Banco\\" é obrigatório"
                         ],
                         "password": [
                           "Senha precisa conter pelo menos um número",
                           "Senha precisa conter pelo menos um caractere maiúsculo",
                           "Senha precisa conter pelo menos um caractere especial"
                         ],
                         "bankAccount.account": [
                           "O campo \\"Conta\\" é obrigatório"
                         ],
                         "bankAccount.agency": [
                           "O campo \\"Agência\\" é obrigatório"
                         ],
                         "name": [
                           "O campo \\"Nome\\" é obrigatório"
                         ],
                         "address.nameAddress": [
                           "Nome do endereço é obrigatório"
                         ],
                         "cnhNumber": [
                           "O número da CNH deve conter entre 9 e 11 dígitos numéricos"
                         ],
                         "cpf": [
                           "O campo \\"CPF\\" deve estar no formato: XXX.XXX.XXX-XX"
                         ],
                         "vehicleDocument": [
                           "O RENAVAM deve conter entre 9 e 11 dígitos numéricos"
                         ],
                         "email": [
                           "O campo \\"E-mail\\" deve estar no formato: nome@dominio.com"
                         ],
                         "cnhValidity": [
                           "CNH fora da validade"
                         ]
                       }
                     }
            """)))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateDeliveryPerson {
}
