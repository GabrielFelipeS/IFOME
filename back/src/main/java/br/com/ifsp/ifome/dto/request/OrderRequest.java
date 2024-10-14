package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.entities.Dish;

public record OrderRequest(
    Long id,
    Dish dish,
    Integer quantity,
    Double price
) {
}
