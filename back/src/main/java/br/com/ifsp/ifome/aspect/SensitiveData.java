package br.com.ifsp.ifome.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcação de métodos que recebem dados sensíveis.
 * Utilizado para restringir a geração de logs do  {@link LoggingAspect} em método marcado com {@code SensiveData}
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {
}
