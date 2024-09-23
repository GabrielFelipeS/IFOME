package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.*;
import br.com.ifsp.ifome.validation.anotations.Past;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@ConfirmartionPasswordEqualsPassword(message = "As senhas não coincidem")
public record DeliveryPersonRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @CPF(message = "CPF inválido")
        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        @NotRegisteredEmailDeliveryPerson
        String email,

        @ValidPassword
        @NotBlank(message = "Senha é obrigatório")
        String password,

        @NotBlank(message = "Confirmação de senha é obrigatório")
        String confirmationPassword,

        @Past(message = "Data de nascimento deve estar no passado")
        @MinAgeToUse
        @NotNull(message = "Data de nascimento é obrigatório")
        @DateFormat(message = "Formato incorreto para data de nascimento")
        String dateOfBirth,

        @Pattern(regexp = "^(carro|moto)$", message = "Tipo do veículo inválido")
        String typeOfVehicle,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telephone,

        // TODO não deveria ser dois campos?
        @NotBlank(message = "Cnh é obrigatório")
        String cnh,

        @NotBlank(message = "Documento do veiculo é obrigatório")
        String vehicleDocument,

        @NotEmpty(message = "É necessário ter pelo menos um endereço")
        List<@Valid AddressRequest> address,

        @Valid
        BankAccountRequest bankAccount
) {
        public LocalDate convertDateOfBirth() {
                return LocalDate.parse(this.dateOfBirth);
        }
}
