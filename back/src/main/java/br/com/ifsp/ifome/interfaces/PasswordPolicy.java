package br.com.ifsp.ifome.interfaces;

import br.com.ifsp.ifome.dto.request.ClientLoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface PasswordPolicy {
    boolean isLoginCorrect(String rawPassword, BCryptPasswordEncoder passwordEncoder);

    default boolean isLoginIncorrect(String rawPassword, BCryptPasswordEncoder passwordEncoder) {
        return !isLoginCorrect(rawPassword, passwordEncoder);
    }
}
