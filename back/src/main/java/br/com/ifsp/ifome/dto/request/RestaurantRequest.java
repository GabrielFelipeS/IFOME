package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.validation.anotations.ConfirmartionPasswordEqualsPassword;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmailRestaurant;
import br.com.ifsp.ifome.validation.anotations.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@ConfirmartionPasswordEqualsPassword(message = "As senhas não coincidem")
public record RestaurantRequest(
        @NotBlank(message = "Nome do restaurante é obrigatório")
        String nameRestaurant,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        @NotRegisteredEmailRestaurant
        String email,

        @ValidPassword
        @NotBlank(message = "Senha é obrigatório")
        String password,
        @NotBlank(message = "Confirmação de senha é obrigatório")
        String confirmationPassword,

        @CNPJ(message = "CNPJ inválido")
        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotEmpty(message = "É necessário ter pelo menos um endereço")
        List<@Valid AddressRequest> address,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telephone,

        @NotBlank(message = "Categoria de comida é obrigatória")
        String foodCategory,

        @NotBlank(message = "Formas de pagamento são obrigatórias")
        String paymentMethods,

        @NotEmpty(message = "Horário de funcionamento é obrigatório")
        List<OpeningHoursRequest> openingHours,

        String personResponsible,

        @CPF(message = "CPF inválido")
        @NotBlank(message = "CPF é obrigatório")
        String personResponsibleCPF,

        String restaurantImage,
        @Valid
        BankAccountRequest bankAccount
) { }
