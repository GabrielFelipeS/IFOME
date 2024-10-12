package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.Past;
import br.com.ifsp.ifome.validation.anotations.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@ConfirmartionPasswordEqualsPassword(message = "As senhas não coincidem")
public record DeliveryPersonRequest(
        @NotBlank(message = "O campo \"Nome\" é obrigatório")
        String name,

        @CPF(message = "O campo \"CPF\" deve estar no formato: XXX.XXX.XXX-XX")
        @NotBlank(message = "O campo \"CPF\"  é obrigatório")
        String cpf,

        @NotRegisteredEmailDeliveryPerson
        @NotBlank(message = "O campo \"E-mail\" é obrigatório")
        @Email(message = "O campo \"E-mail\" deve estar no formato: nome@dominio.com")
        String email,

        @ValidPassword
        @NotBlank(message = "O campo \"Senha\" é obrigatório")
        String password,

        @NotBlank(message = "O campo \"Confirmação de senha\" é obrigatório")
        String confirmationPassword,

        @MinAgeToUse
        @NotNull(message = "Data de nascimento é obrigatório")
        @Past(message = "Data de nascimento deve estar no passado")
        @DateFormat(message = "Formato incorreto para data de nascimento")
        @MaxAgeToUse(maxAge = 90, message = "Para cadastro no sistema, é necessário ter no máximo {maxAge} anos de idade.")
        String dateOfBirth,

        @Pattern( message = "Tipo do veículo inválido, tipos de veiculos aceitos: carro e moto", regexp = "^([Cc]arro|[Mm]oto)$")
        String typeOfVehicle,

        @NotBlank(message = "O campo \"Placa\" é obrigatório")
        @Pattern(regexp = "[A-Z]{3}-\\d{4}", message = "A placa deve estar no formato XXX-9999")
        String plate,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telephone,

        @UniqueCnhNumber
        @NotBlank(message = "O campo \"Número do CNH\" é obrigatório")
        @Pattern(regexp = "\\d{9,11}", message = "O número da CNH deve conter entre 9 e 11 dígitos numéricos")
        String cnhNumber,


        @NotNull(message = "O campo \"validade\" é obrigatório")
        @Future(message = "CNH fora da validade")
        LocalDate cnhValidity,

        @NotBlank(message = "O campo \"documento do veículo\" é obrigatório")
        @Pattern(regexp = "\\d{9,11}", message = "O RENAVAM deve conter entre 9 e 11 dígitos numéricos")
        String vehicleDocument,

        @Valid
        @NotNull(message = "É necessário ter um endereço")
        AddressRequest address,

        @Valid
        @NotNull(message = "É necessário ter ao menos uma conta")
        BankAccountRequest bankAccount
) {
        public LocalDate convertDateOfBirth() {
                return LocalDate.parse(this.dateOfBirth);
        }
}
