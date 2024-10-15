package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
    //Long id,
    Integer dishId, // Dessa forma vamos conseguir buscar o prato no banco de dados, com os valores verdadeiros dele

    @NotNull(message = "Insira a quatidade do pedido")
    @Positive(message = "O quantidade deve conter apenas valores númericos e positivos")
    Integer quantity
) {
}
