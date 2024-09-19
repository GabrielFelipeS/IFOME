package br.com.ifsp.ifome.docs;


import br.com.ifsp.ifome.dto.request.AddressRequest;
import br.com.ifsp.ifome.dto.request.BankAccountRequest;
import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import br.com.ifsp.ifome.dto.response.RestaurantResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Operation(summary = "Cadastrar um novo restaurante")
@RequestBody(
        description = "Detalhes do restaurante a ser criado",
        required = true,
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantRequest.class),
                examples = {
                @ExampleObject(
                                name = "Restaurante válido",
                                description = "Inserindo restaurante válido",
                                value = """
                    {
                      "nameRestaurant" : "Nome Restaurante",
                      "email" : "email@email.com",
                      "password" : "@Senha1",
                      "confirmationPassword" : "@Senha1",
                      "cnpj" : "10.882.594/0001-65",
                      "address": [
                        {
                          "nameAddress": "casa principal",
                          "cep": "35170-222",
                          "neighborhood": "neighborhood",
                          "city": "Coronel Fabriciano",
                          "state": "Minas Gerais",
                          "address": "address",
                          "complement": "complement",
                          "number": "12",
                          "details": "details"
                        }
                      ],
                      "telephone" : "(11) 1234-5678",
                      "foodCategory" : "Pizzaria",
                      "paymentMethods" : "Dinheiro, Cartão",
                      "openingHoursStart" : "12:00",
                      "openingHoursEnd" : 23:00",
                      "personResponsible" : "responsavel",
                      "personResponsibleCPF" : "033.197.356-16",
                      "restaurantImages" : "imagem.jpeg",
                      "bankAccount" : [
                          {
                              "bank" : "123",
                              "agency" : "1255",
                              "account" : "4547-7"
                           }
                      ]
                    }
                """
                        ),
                        @ExampleObject(
                                name = "Restaurante inválido",
                                description =
                                        """
                                            Inserindo restaurante inválido
                                            cnpj: fora do padrão
                                            personResponsibleCPF: fora do padrão
                                            email: não possui @
                                            password: não possui letra maiúscula, número e caractere especial
                                            address: esta vazio
                                            bankAccount: esta vazio
                                        """,
                                value = """
                    {
                        "personResponsibleCPF": "4860867801",
                        "cnpj" : "155455554"
                        "email" : "testeemail.com",
                        "password": "password",
                        "confirmationPassword": "password",
                        "bankAccount": [],
                        "address": []
                    }
                    """
                        )

                }
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = br.com.ifsp.ifome.dto.ApiResponse.class),
                        examples =  @ExampleObject(value = """
                {
                    "status": "success",
                    "data": {
                         "id": 2,
                          "nameRestaurant" : "Nome Restaurante",
                      "email" : "email@email.com",
                      "password" : "@Senha1",
                      "confirmationPassword" : "@Senha1",
                      "cnpj" : "10.882.594/0001-65",
                      "address": [
                        {
                          "nameAddress": "casa principal",
                          "cep": "35170-222",
                          "neighborhood": "neighborhood",
                          "city": "Coronel Fabriciano",
                          "state": "Minas Gerais",
                          "address": "address",
                          "complement": "complement",
                          "number": "12",
                          "details": "details"
                        }
                      ],
                      "telephone" : "(11) 1234-5678",
                      "foodCategory" : "Pizzaria",
                      "paymentMethods" : "Dinheiro, Cartão",
                      "openingHoursStart" : "12:00",
                      "openingHoursEnd" : 23:00",
                      "personResponsible" : "responsavel",
                      "personResponsibleCPF" : "033.197.356-16",
                      "restaurantImages" : "imagem.jpeg",
                      "bankAccount" : [
                          {
                              "bank" : "123",
                              "agency" : "1255",
                              "account" : "4547-7"
                           }
                      ]
                    },
                    "message": "Restaurante criado com sucesso"
                }
            """)
                )),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = RestaurantResponse.class),
                        examples = @ExampleObject(value = """
            {
                "password": [
                    "Senha precisa conter pelo menos um caractere especial",
                    "Senha precisa conter pelo menos um número",
                    "Senha precisa conter pelo menos um caractere maiúsculo"
                ],
                "address": [
                    "não deve estar vazio"
                ],
                "telephone": [
                    "não deve estar em branco"
                ],
                "cnpj": [
                    "cnpj inválido"
                ],
                "email": [
                    "E-mail inválido"
                ],
                 "restaurantRequest": [
                     "Senha e confirmar senha não são iguais"
                 ]
                 "bankAccount" : [
                    "não deve estar em branco"
                 ]
                 "foodCategory" : [
                    "não deve estar em branco"
                 ]
                  "paymentMethods" :[
                    "não deve estar em branco"
                  ],
                  "openingHoursStart" :[
                    "não deve estar em branco"
                  ]
                  "openingHoursEnd" :[
                    "não deve estar em branco"
                  ]
                  "personResponsible" : [
                    "O nome não deve estar em branco"
                  ]
                  "personResponsibleCPF" : [
                    "não deve estar em branco"
                  ]
            }
            """)))
})

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateRestaurant { }
