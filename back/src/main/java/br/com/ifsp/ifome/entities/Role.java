package br.com.ifsp.ifome.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.ifsp.ifome.entities.Permission.*;

@RequiredArgsConstructor
public enum Role {
    BASIC(Collections.emptySet()),
    CLIENT(
        Set.of(
            CLIENT_CREATE,
            CLIENT_DELETE,
            CLIENT_UPDATE,
            CLIENT_READ
        )
    ),
    RESTAURANT(
        Set.of(
            RESTAURANT_CREATE,
            RESTAURANT_READ,
            RESTAURANT_DELETE,
            RESTAURANT_UPDATE
        )
    ),
    DELIVERY(
        Set.of(
            DELIVERY_CREATE,
            DELIVERY_READ,
            DELIVERY_UPDATE,
            DELIVERY_DELETE
        )
    ),
    ADMIN(
        Set.of(
            CLIENT_CREATE,
            CLIENT_DELETE,
            CLIENT_UPDATE,
            CLIENT_READ,
            RESTAURANT_CREATE,
            RESTAURANT_READ,
            RESTAURANT_DELETE,
            RESTAURANT_UPDATE,
            DELIVERY_CREATE,
            DELIVERY_READ,
            DELIVERY_UPDATE,
            DELIVERY_DELETE
        )
    ),
    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.name()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
