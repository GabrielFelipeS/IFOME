package br.com.ifsp.ifome.dto.response;

import br.com.ifsp.ifome.entities.*;

import java.util.List;
// TODO colocar atributo de totalPriceOrder e arrumar o freigth
public record CartResponse(
        List<OrderItem> orderItems,
        Double freigth,
        Double totalPrice,
        Integer totalQuantity
) {
    public CartResponse (Cart cart){
        this(
            cart.getOrderItems(),
//            cart.getFreight(),
            0.0,
            cart.totalPrice(),
            cart.totalQuantity()
        );
    }
}
