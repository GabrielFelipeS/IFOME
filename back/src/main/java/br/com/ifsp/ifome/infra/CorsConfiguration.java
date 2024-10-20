package br.com.ifsp.ifome.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // ou o endpoint que você deseja permitir
            .allowedOrigins("*") // Altere para a origem do seu frontend
            .allowedMethods("*")
            .allowedHeaders("*")
        ;
    }
}
