package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
    //Long id,
    @NotNull(message = "Insira o id do prato")
    @Positive(message = "DishID deve ser maior que zero")
    Long dishId, // Dessa forma vamos conseguir buscar o prato no banco de dados, com os valores verdadeiros dele

    @NotNull(message = "Insira a quantidade do pedido")
    @Positive(message = "O quantidade não pode ser 0 e deve conter apenas valores númericos e positivos")
    Integer quantity,

    String detail
) {
}
