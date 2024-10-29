package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.aspect.SensiveData;
import br.com.ifsp.ifome.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/token")
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @PostMapping
    @SensiveData
    @Operation(summary = "Verificar se esta logado", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<String> validToken(Authentication authentication) {
        if(authentication.isAuthenticated()) {
            return ResponseEntity.ok("Tokén válido");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @SensiveData
    @PostMapping("/restaurant/")
    @Operation(summary = "Verificar se esta logado como restaurante", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<String> validTokenRestaurant(Authentication authentication) {
       boolean valid = authentication.getAuthorities().stream()
            .peek(role -> System.out.println(role.getAuthority()))
            .anyMatch(role -> role.getAuthority().equals("ROLE_RESTAURANT"));
        if(valid) {
            return ResponseEntity.ok("Token válido para restaurante");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @SensiveData
    @PostMapping("/client/")
    @Operation(summary = "Verificar se esta logado como cliente", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<String> validTokenClient(Authentication authentication) {
        boolean valid = authentication.getAuthorities().stream()
            .peek(role -> System.out.println(role.getAuthority()))
            .anyMatch(role -> role.getAuthority().equals("ROLE_CLIENT"));

        if(valid) {
            return ResponseEntity.ok("Token válido para cliente");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @SensiveData
    @PostMapping("/delivery/")
    @Operation(summary = "Verificar se esta logado como entregador", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<String> validTokenDelivery(Authentication authentication) {
        boolean valid = authentication.getAuthorities().stream()
            .peek(role -> System.out.println(role.getAuthority()))
            .anyMatch(role -> role.getAuthority().equals("ROLE_DELIVERY"));

        if(valid) {
            return ResponseEntity.ok("Token válido para delivery");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
