package br.com.ifsp.ifome.services;

import br.com.ifsp.ifome.aspect.SensitiveData;
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

    /**
     * Verifica se o usuário alvo da ação de login existe e se a senha bruta e criptografada coincidem
     *
     * @param passwordPolicyOptional Usuário alvo da ação de login
     * @param rawPassword Senha bruta, sem ser criptografada
     * @param bCryptPasswordEncoder Encriptador de senha a ser usado
     */
    @SensitiveData
    public void isLoginIncorrect(Optional<? extends PasswordPolicy> passwordPolicyOptional, String rawPassword , BCryptPasswordEncoder bCryptPasswordEncoder) {
        if(passwordPolicyOptional.isEmpty() || passwordPolicyOptional.get().isLoginIncorrect(rawPassword, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("email ou senha incorretos!");
        }
    }

    /**
     * Gera um token para recuperar a senha
     *
     * @param client Informações do cliente
     * @return Token gerado para o cliente
     * @throws Exception Caso não consiga pegar o objeto de SecureRandomFactoryBean
     */
    @SensitiveData
    public String generateTokenForgotPassword(Client client) throws Exception {
        KeyBasedPersistenceTokenService tokenService = getInstanceFor(client.getPassword());

        Token token = tokenService.allocateToken(client.getEmail());

        return token.getKey();
    }

    /**
     * Gera um servico se persistencia de token baseado em chave usando como {@code serverSecret} a senha atual
     *
     * @param password Senha atual
     * @return Servico se persistencia de token baseado em chave
     * @throws Exception  Caso não consiga pegar o objeto de SecureRandomFactoryBean
     */
    @SensitiveData
    private KeyBasedPersistenceTokenService getInstanceFor(String password) throws Exception {
        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();

        tokenService.setServerSecret(password);
        tokenService.setServerInteger(16);
        tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
        return tokenService;
    }
}
