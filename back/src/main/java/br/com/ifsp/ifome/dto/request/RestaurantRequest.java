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
        @NotBlank(message = "O campo \"Nome do restaurante\" é obrigatório")
        String nameRestaurant,

        @NotRegisteredEmailRestaurant
        @NotBlank(message = "O campo \"E-mail\" é obrigatório")
        @Email(message = "E-mail deve estar no formato: nome@dominio.com")
        String email,

        @ValidPassword
        @NotBlank(message = "Senha é obrigatório")
        String password,
        @NotBlank(message = "Confirmação de senha é obrigatório")
        String confirmationPassword,

        @CNPJ(message = "O campo \"CNPJ\" deve estar no formato XX.XXX.XXX/XXXX-XX")
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
        List<@Valid OpeningHoursRequest> openingHours,

        String personResponsible,

        @CPF(message = "O campo \"CPF\" deve estar no formato: XXX.XXX.XXX-XX")
        @NotBlank(message = "CPF é obrigatório")
        String personResponsibleCPF,

        @Valid
        BankAccountRequest bankAccount
) { }
