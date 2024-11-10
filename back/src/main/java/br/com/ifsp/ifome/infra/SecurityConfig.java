package br.com.ifsp.ifome.infra;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**"))
            .authorizeHttpRequests(request ->
                request
                    .requestMatchers("/actuator/**", "/api/auth/**", "/api/image/**", "/h2-console/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/dish").hasRole("RESTAURANT")
                    .requestMatchers(HttpMethod.GET,
                "/api/restaurant/", "/api/restaurant/{id}","/api/restaurant/all", "/api/dish/", "/api/dish/{id}", "/api/dish/restaurant/{id}","/api/dish/all")
                    .permitAll()

                    .requestMatchers("/api/client/**").hasRole("CLIENT")
                    .requestMatchers(HttpMethod.PUT,"/api/order/teste").permitAll()

                    .requestMatchers(HttpMethod.PATCH, "/api/restaurant/").hasRole("RESTAURANT")
                    .requestMatchers(HttpMethod.PUT, "/api/order/updateStatus/{customerOrderId}").hasRole("RESTAURANT")
                    .requestMatchers(HttpMethod.GET, "/api/order/restaurantOrders").hasRole("RESTAURANT")
                    .requestMatchers(HttpMethod.GET, "/api/restaurant/orders").hasRole("RESTAURANT")
                    .requestMatchers(HttpMethod.GET, "/api/order/customerOrders").hasRole("CLIENT")
                    .requestMatchers(HttpMethod.GET, "/api/delivery/orders/").hasRole("DELIVERY")

                    .requestMatchers(
                        HttpMethod.PUT,
                        "/api/restaurant/",
                        "/api/restaurant/order/status/{customerOrderId}",
                        "/api/restaurant/order/status/{customerOrderId}/previous")
                    .hasRole("RESTAURANT")

                    .anyRequest().authenticated())
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(auth02 -> auth02.jwt(Customizer.withDefaults()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // Define o claim para authorities
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Não adicionar prefixo "ROLE_"

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
