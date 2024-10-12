package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@ConfirmartionPasswordEqualsPassword(message = "A senha e confirmação de senha não coincidem")
public record ClientRequest (
        @NotBlank(message = "O campo \"Nome\" é obrigatório")
        String name,

        @NotRegisteredEmail
        @NotBlank(message = "O campo \"E-mail\" é obrigatório")
        @Email(message = "E-mail deve estar no formato: nome@dominio.com")
        String email,

        @ValidPassword
        @NotBlank(message = "O campo \"Senha\" é obrigatório")
        String password,

        @NotBlank(message = "O campo \"Confirmação da senha\" é obrigatório")
        String confirmationPassword,

        @NotNull(message = "O campo \"Data de nascimento\" é obrigatório")
        @Past(message = "Data de nascimento deve estar no passado")
        @MinAgeToUse(minAge = 13, message = "Para cadastro no sistema, é necessário ter pelo menos {minAge} anos de idade.")
        @MaxAgeToUse(maxAge = 90, message = "Para cadastro no sistema, é necessário ter no máximo {maxAge} anos de idade.")
        @DateFormat(message = "Data de nascimento deve estar no formato yyyy-mm-dd, atualmente está: {validatedValue}")
        String dateOfBirth,

        @CPF(message = "CPF inválido")
        @NotBlank(message = "O campo \"CPF\" é obrigatório")
        String cpf,

        @NotBlank(message = "O campo \"Telefone\" é obrigatório")
        @Pattern(message = "Telefone deve estar no formato (XX) XXXXX-XXXX", regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}")
        String phone,

        @Valid
        @NotNull(message = "É necessário ter um endereço")
        AddressRequest address
) {
        public LocalDate convertDateOfBirth() {
                return LocalDate.parse(this.dateOfBirth);
        }
}
