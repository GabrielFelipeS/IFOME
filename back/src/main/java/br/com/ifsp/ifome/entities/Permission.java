package br.com.ifsp.ifome.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    CLIENT_READ("client:read"),
    CLIENT_UPDATE("client:update"),
    CLIENT_CREATE("client:create"),
    CLIENT_DELETE("client:delete"),

    RESTAURANT_READ("restaurant:read"),
    RESTAURANT_UPDATE("restaurant:update"),
    RESTAURANT_CREATE("restaurant:create"),
    RESTAURANT_DELETE("restaurant:delete"),

    DELIVERY_READ("delivery:read"),
    DELIVERY_UPDATE("delivery:update"),
    DELIVERY_CREATE("delivery:create"),
    DELIVERY_DELETE("delivery:delete"),
    ;

    @Getter
    private final String permission;
}
