package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.SensitiveData;
import br.com.ifsp.ifome.exceptions.global.InvalidTokenException;
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

    /**
     * Cria um token colocando o email do usuário, permissões e o tempo de expiração
     *
     * @param email Email do usuário a ser logado
     * @param grantedAuthorities Permissões que o usuário vai possuir
     * @return Token gerado
     */
    @SensitiveData
    public String generateToken(String email, Collection<? extends GrantedAuthority> grantedAuthorities) {
        var authorities = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
        var claims = JwtClaimsSet.builder()
            .issuer("api_ifome")
            .subject(email)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("authorities", authorities)
            .build()
            ;

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     *  Valida o token passado como parâmetro
     *
     * @param token Token a ser validado
     * @throws InvalidTokenException Quando o token é inválido
     */
    @SensitiveData
    public void validToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}
