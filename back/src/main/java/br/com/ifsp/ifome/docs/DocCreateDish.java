package br.com.ifsp.ifome.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Operation(summary = "Cadastrar um novo prato",  security = @SecurityRequirement(name = "Bearer Token"))
//@RequestBody(
//        description = "Detalhes do prato cadastrado",
//        required = true,
//        content = @Content(
//                mediaType = "multipart/form-data",
//                examples ={
//                        @ExampleObject(
//                                name = "Prato válido",
//                                description = "Inserindo um prato válido",
//                                value = """
//                                {
//                                    "name" : "Lasanha",
//                                    "description" : "Massa fresca e molho bolonhesa",
//                                    "price" : "45.0",
//                                    "dishCategory": "Massa",
//                                    "dishImage" : "image.jpg",
//                                    "availability" : "Disponível"
//                                }
//                                """
//                        ),
//                        @ExampleObject(
//                                name = "Prato inválido",
//
//
//                                description = """
//                                        name: "O nome do prato é obrigatório,
//                                        price: [
//                                            Insira um valor de preço válido,
//                                            O preço é obrigatório,
//                                            Insira um valor de preço válido
//                                            ],
//                                        description = A descrição do prato é obrigatória,
//                                        dishCategory = A categoria do prato é obrigatória,
//                                        availability = A disponibilidade é obrigatória
//
//                                        """,
//                                value = """
//                                        {
//                                        "name" : "",
//                                        "price" : -450,
//                                        "description" = "",
//                                        "dishCategory" = "",
//                                        "availability" = ""
//                                        }
//                                        """
//                        )
//                }
//        )
//
//)
//
//@ApiResponses({
//        @ApiResponse(responseCode =  "201", description = "Prato cadastrado com sucesso",
//                content = @Content(mediaType = "application/json",
//                        schema = @Schema(implementation =  br.com.ifsp.ifome.dto.ApiResponse.class),
//                        examples =  @ExampleObject(
//                                value = """
//                        {
//                            "status" : "success",
//                            "data": {
//                                "id": 2,
//                                "name" : "Lasanha",
//                                "description" : "Massa fresca e molho bolonhesa",
//                                "price" : "45.0",
//                                "dishCategory": "Massa",
//                                "dishImage" : "image.jpg",
//                                "availability" : "Disponível"
//                            },
//                             "message": "Prato criado com sucesso"
//
//                        }
//                        """)
//                )),
//        @ApiResponse(responseCode = "400", description = "invalid request",
//                content = @Content(mediaType = "application/json",
//                        schema = @Schema(implementation = DishResponse.class),
//                        examples = @ExampleObject(
//                                value = """
//                                    {
//                                        "name": [
//                                            "O nome do prato é obrigatório"
//                                        ],
//                                        "description": [
//                                            "A descrição do prato é obrigatória"
//                                        ],
//                                        "price" : [
//                                            "O preço deve conter apenas valores númericos e positivos",
//                                            "O preço é obrigatório"
//                                        ],
//                                        "dishCategory" : [
//                                            "A categoria do prato é obrigatória"
//                                        ],
//                                        "availability": [
//                                            "A disponibilidade é obrigatória
//                                         ]
//                                    }
//                 """)
//                )
//        )
//})



@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocCreateDish {
}
