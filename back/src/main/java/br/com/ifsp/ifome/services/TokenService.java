package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private Instant now;
    private Long expiresIn;

    @Autowired
    public TokenService(JwtEncoder jwtEncoder) {
        this(jwtEncoder, Instant.now(), 300L);
    }

    public TokenService(JwtEncoder jwtEncoder, Instant now, Long expiresIn) {
        this.jwtEncoder = jwtEncoder;
        this.now = now;
        this.expiresIn =expiresIn;
    }

    public void isLoginIncorrect(Optional<? extends PasswordPolicy> passwordPolicyOptional, String rawPassword , BCryptPasswordEncoder bCryptPasswordEncoder) {
        if(passwordPolicyOptional.isEmpty() || passwordPolicyOptional.get().isLoginIncorrect(rawPassword, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("email or password is invalid!");
        }
    }

    public String generateToken(String subject) {
        var claims = JwtClaimsSet.builder()
            .issuer("api_ifome")
            .subject(subject)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .build()
            ;

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
