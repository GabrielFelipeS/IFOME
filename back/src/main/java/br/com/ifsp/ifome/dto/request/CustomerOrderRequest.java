package br.com.ifsp.ifome.dto.request;

import java.util.List;

public record CustomerOrderRequest(
        List<OrderItemRequest> items

) {
}
