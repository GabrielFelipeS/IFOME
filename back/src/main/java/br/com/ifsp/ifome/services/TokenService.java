package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.exceptions.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final Long expiresIn;
    private final Instant now;
    //private static final Long DEFAULT_EXPIRES_IN = 3600L;
    private static final Long DEFAULT_EXPIRES_IN = 5259600L; // PARA FRONT TESTAR, 2 meses para expirar

    @Autowired
    public TokenService(JwtEncoder jwtEncoder, @Qualifier("jwtDecoder") JwtDecoder jwtDecoder) {
        this(jwtEncoder, jwtDecoder, Instant.now(), DEFAULT_EXPIRES_IN);
    }

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, Instant now, Long expiresIn) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.expiresIn =expiresIn;
        this.now = now;
    }

    public String generateToken(String subject, Collection<? extends GrantedAuthority> grantedAuthorities) {
        var authorities = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
        var claims = JwtClaimsSet.builder()
            .issuer("api_ifome")
            .subject(subject)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("authorities", authorities)
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
