package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.MinAgeToUse;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmail;
import br.com.ifsp.ifome.validation.anotations.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
// TODO talvez seja necessario inserir a anotação que verifica se password e confirmationPassword são iguais
public record ClientRequest (
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        @NotRegisteredEmail
        String email,

        @ValidPassword
        String password,
        String confirmationPassword,

        @Past(message = "Data de nascimento deve estar no passado")
        @MinAgeToUse(minAge = 14, message = "É necessário ter pelo menos 13 anos")
        @NotNull(message = "Data de nascimento é obrigatório")
        LocalDate dateOfBirth,

        @CPF(message = "CPF inválido")
        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @Valid
        AddressRequest address,
        String paymentMethods
) { }
