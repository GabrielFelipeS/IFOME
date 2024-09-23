package br.com.ifsp.ifome.interfaces;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordPolicy {
    boolean isLoginCorrect(String rawPassword, PasswordEncoder passwordEncoder);

    default boolean isLoginIncorrect(String rawPassword, PasswordEncoder passwordEncoder) {
        return !isLoginCorrect(rawPassword, passwordEncoder);
    }
}
