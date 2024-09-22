package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.ConfirmartionPasswordEqualsPassword;
import br.com.ifsp.ifome.validation.anotations.MinAgeToUse;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmail;
import br.com.ifsp.ifome.validation.anotations.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@ConfirmartionPasswordEqualsPassword(message = "As senhas não coincidem")
public record ClientRequest (
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        @NotRegisteredEmail
        String email,

        @ValidPassword
        @NotBlank(message = "Senha é obrigatório")
        String password,

        @NotBlank(message = "Confirmação da senha é obrigatório")
        String confirmationPassword,

        @Past(message = "Data de nascimento deve estar no passado")
        @MinAgeToUse(minAge = 13, message = "Para cadastro no sistema, é necessário ter pelo menos 13 anos de idade.")
        @NotNull(message = "Data de nascimento é obrigatório")
        LocalDate dateOfBirth,

        @CPF(message = "CPF inválido")
        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "^\\([1-9]{2}\\) (?:[2-8]|9[0-9])[0-9]{3}-[0-9]{4}$")
        String phone,

        @NotEmpty(message = "É necessário ter pelo menos um endereço")
        List<@Valid AddressRequest> address
) { }
