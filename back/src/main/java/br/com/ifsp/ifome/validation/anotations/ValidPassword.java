package br.com.ifsp.ifome.validation.anotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Size(min = 6, message = "A senha precisa possui pelo menos 6 caracteres")
@Pattern(regexp = ".*\\d+.*", message = "Senha precisa conter pelo menos um número")
@Pattern(regexp = ".*[A-ZÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+.*", message = "Senha precisa conter pelo menos um caractere maiúsculo")
@Pattern(regexp = ".*[a-záàâãéèíïóôõöúçñ]+.*", message = "Senha precisa conter pelo menos um caractere minúsculo")
@Pattern(regexp = ".*[^\\w\\s]+.*", message = "Senha precisa conter pelo menos um caractere especial")
@Pattern(regexp = ".*[^\\s].*", message = "Senha não pode conter espaço")
public @interface ValidPassword {
    String message() default "Senha inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

