package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.exceptions.InvalidTokenException;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final Long expiresIn;
    private final Instant now;

    @Autowired
    public TokenService(JwtEncoder jwtEncoder, @Qualifier("jwtDecoder") JwtDecoder jwtDecoder) {
        this(jwtEncoder, jwtDecoder, Instant.now(), 3600L);
    }

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, Instant now, Long expiresIn) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.expiresIn =expiresIn;
        this.now = now;
    }

    public String generateToken(String subject) {
        var claims = JwtClaimsSet.builder()
            .issuer("api_ifome")
            .subject(subject)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(2629800)) // PARA FRONT TESTAR, 1 mes para expirar
            .build()
            ;

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void validToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}
