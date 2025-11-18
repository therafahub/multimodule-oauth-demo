package com.microservices.webui.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom Authentication Provider que valida credenciales contra Auth Service
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final WebClient webClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.info("🔐 Validando credenciales para usuario: {}", username);

        try {
            // Llamar a Auth Service para validar credenciales
            AuthResponse authResponse = webClient.post()
                .uri("/auth/api/v1/auth/validate")
                .bodyValue(new AuthRequest(username, password))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();

            if (authResponse == null || !authResponse.isValid()) {
                log.warn("❌ Credenciales inválidas para usuario: {}", username);
                throw new BadCredentialsException("Usuario o contraseña inválidos");
            }

            log.info("✅ Autenticación exitosa para usuario: {}", username);

            // Convertir roles a GrantedAuthority
            Collection<? extends GrantedAuthority> authorities = authResponse.getRoles() != null ?
                authResponse.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList()) :
                Collections.emptyList();

            return new UsernamePasswordAuthenticationToken(
                username,
                password,
                authorities
            );

        } catch (WebClientResponseException.Unauthorized e) {
            log.warn("❌ Credenciales rechazadas por Auth Service: {}", e.getStatusCode());
            throw new BadCredentialsException("Usuario o contraseña inválidos", e);
        } catch (WebClientResponseException.NotFound e) {
            log.warn("❌ Usuario no encontrado: {}", username);
            throw new BadCredentialsException("Usuario o contraseña inválidos", e);
        } catch (WebClientResponseException e) {
            log.error("❌ Error de Auth Service ({}): {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BadCredentialsException("Error en autenticación: " + e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("❌ Error inesperado durante autenticación", e);
            e.printStackTrace();
            throw new BadCredentialsException("Error interno de autenticación: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // DTOs para comunicación con Auth Service
    public static class AuthRequest {
        private String username;
        private String password;

        public AuthRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthResponse {
        private boolean valid;
        private String username;
        private List<String> roles;

        public AuthResponse() {}

        public AuthResponse(boolean valid, String username, List<String> roles) {
            this.valid = valid;
            this.username = username;
            this.roles = roles;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
