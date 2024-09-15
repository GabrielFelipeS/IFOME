package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.ConfirmartionPasswordEqualsPassword;
import br.com.ifsp.ifome.validation.anotations.ValidPassword;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmailDeliveryPerson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@ConfirmartionPasswordEqualsPassword
public record DeliveryPersonRequest(
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

        LocalDate dateOfBirth,
        String typeOfVehicle,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telephone,

        @NotBlank(message = "CNH obrigatória")
        String cnh,
        @NotBlank
        String vehicleDocument,

        @NotEmpty
        List<@Valid AddressRequest> address,
        @Valid
        BankAccountRequest bankAccount
) {

}
