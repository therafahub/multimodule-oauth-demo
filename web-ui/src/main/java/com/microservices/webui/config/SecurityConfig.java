package com.microservices.webui.config;

import com.microservices.webui.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;

/**
 * Configuración de seguridad y WebClient
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .authenticationManager(authenticationManager)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/auth/login", "/auth/register", "/css/**", "/js/**", "/images/**", "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/actuator/**")
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
        return new ProviderManager(Arrays.asList(customAuthenticationProvider));
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("http://auth-service:8081")
            .build();
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // Permitir ";" en las URLs (para JSESSIONID)
        firewall.setAllowSemicolon(true);
        return firewall;
    }
}
