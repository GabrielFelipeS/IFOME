package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.interfaces.PasswordPolicy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    public void isLoginIncorrect(Optional<? extends PasswordPolicy> passwordPolicyOptional, String rawPassword , BCryptPasswordEncoder bCryptPasswordEncoder) {
        if(passwordPolicyOptional.isEmpty() || passwordPolicyOptional.get().isLoginIncorrect(rawPassword, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("email or password is invalid!");
        }
    }
}
