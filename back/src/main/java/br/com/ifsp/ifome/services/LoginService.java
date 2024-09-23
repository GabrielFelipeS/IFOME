package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.entities.Client;
import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    public void isLoginIncorrect(Optional<? extends PasswordPolicy> passwordPolicyOptional, String rawPassword , BCryptPasswordEncoder bCryptPasswordEncoder) {
        if(passwordPolicyOptional.isEmpty() || passwordPolicyOptional.get().isLoginIncorrect(rawPassword, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("email ou senha incorretos!");
        }
    }

    public String generateTokenForgotPassword(Client client) throws Exception {
        KeyBasedPersistenceTokenService tokenService = getInstanceFor(client.getPassword());

        Token token = tokenService.allocateToken(client.getEmail());

        return token.getKey();
    }

    private KeyBasedPersistenceTokenService getInstanceFor(String password) throws Exception {
        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();

        tokenService.setServerSecret(password);
        tokenService.setServerInteger(16);
        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
        return tokenService;
    }
}
