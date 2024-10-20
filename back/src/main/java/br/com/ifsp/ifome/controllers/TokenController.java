package br.com.ifsp.ifome.controllers;

import br.com.ifsp.ifome.aspect.SensiveData;
import br.com.ifsp.ifome.services.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<String> validToken(@Valid @NotBlank @RequestBody String token) {
        tokenService.validToken(token);
        return ResponseEntity.ok("Token válido");
    }
}
