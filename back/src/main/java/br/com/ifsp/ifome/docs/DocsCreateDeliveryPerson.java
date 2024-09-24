package br.com.ifsp.ifome.docs;


import br.com.ifsp.ifome.dto.request.RestaurantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Operation(summary = "Cadastrar um novo Entregador")
@RequestBody(
        description = "Detalhes do entregador a ser criado",
        required = true,
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantRequest.class),
                examples = {
                        @ExampleObject(
                                name = "Entregador válido",
                                description = "Inserindo entregador válido",
                                value = """
                    {
                      "name" : "Nome entregador",
                      "cpf" : "033.197.356-16"
                      "email": "email@email.com",
                      "password": "@Senha1",
                      "confirmationPassword" : "@Senha1",
                      "dateOfBirth": "1999-02-01",
                      "typeOfVehicle" : "Carro",
                      "plate" : "DIT-4987"
                      "telephone" : "(11) 1234-5678",
                      "cnhNumber" : "document.jpg"
                      "cnhValidity": "2028-1-2",
                      "vehicleDocument" : "111456789",
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
                          "typeResidence" : "Casa",
                          "details": "details"
                        }
                      ],
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
                                name = "entregador inválido",
                                description =
                                        """
                                            Inserindo entregador inválido
                                            cpf: fora do padrão
                                            email: não possui @
                                            password: não possui letra maiúscula, número e caractere especial
                                            address: esta vazio
                                            bankAccount: esta vazio
                                            plate:  "Verique a placa"
                                        """,
                                value = """
                    {
                        "cpf": "4860867801",
                        "email" : "testeemail.com",
                        "password": "password",
                        "confirmationPassword": "password",
                        "bankAccount": [],
                        "address": []
                        "plate": "DIT4987"
                    }
                    """
                        )

                }
        )
)

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocsCreateDeliveryPerson {
}
