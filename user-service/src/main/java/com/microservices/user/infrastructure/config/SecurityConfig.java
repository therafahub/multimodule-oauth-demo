package com.microservices.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para Resource Server
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/actuator/prometheus").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
            );

        return http.build();
    }

    private static class JwtAuthenticationConverter extends org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter {
        public JwtAuthenticationConverter() {
            var authoritiesConverter = new org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter();
            authoritiesConverter.setAuthoritiesClaimName("roles");
            authoritiesConverter.setAuthorityPrefix("ROLE_");
            setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        }
    }
}
