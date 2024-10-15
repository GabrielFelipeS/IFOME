package br.com.ifsp.ifome.dto.request;

import jakarta.validation.constraints.Min;

public record OrderRequest(
    Long id,
//  Dish dish, - Usuario não deve ter a capacidade de enviar o prato se não ele pode alterar outras informações como o id do prato
//  Double price - Usuario não deve ter a capacidade de enviar o preço, se não ele envia 0.0
    Integer dishId, // Dessa forma vamos conseguir buscar o prato no banco de dados, com os valores verdadeiros dele
    // Só que o usuario pode enviar um id invalido, então ter uma validação para caso isso ocorra
    Integer quantity
) {
}
