package br.com.ifsp.ifome.dto.request;

import br.com.ifsp.ifome.entities.BankAccount;
import br.com.ifsp.ifome.validation.anotations.NotRegisteredEmail;
import br.com.ifsp.ifome.validation.anotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CNPJ;

public record RestaurantRequest(
        String nameRestaurant,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        @NotRegisteredEmail
        String email,

        @ValidPassword
        String password,
        String confirmationPassword,

        @CNPJ(message = "CNPJ inválido")
        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "Endereço é obrigatório")
        String address,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telephone,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato XXXXX-XXX")
        String cep,

        @NotBlank(message = "Categoria de comida é obrigatória")
        String foodCategory,

        @NotBlank(message = "Formas de pagamento são obrigatórias")
        String paymentMethods,

        @NotBlank(message = "Horário de funcionamento inicial é obrigatório")
        String openingHoursStart,

        @NotBlank(message = "Horário de funcionamento final é obrigatório")
        String openingHoursEnd,

        String personResponsible,

        String personResponsibleCPF,
        String restaurantImage,
        BankAccount bankAccount
) { }
