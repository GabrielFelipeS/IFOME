package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.IsOfLegalAge;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmail;
import br.com.ifsp.ifome.validation.anotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
        @IsOfLegalAge(message = "É necessário ter pelo menos 18 anos")
        LocalDate dateOfBirth,

        @CPF(message = "CPF inválido")
        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        String typeResidence,
        String cep,
        String address,
        String paymentMethods
) { }
