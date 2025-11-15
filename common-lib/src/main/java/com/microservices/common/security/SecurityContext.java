package com.microservices.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Contexto de seguridad para el usuario autenticado
 */
@Getter
public class SecurityContext {

    private final String userId;
    private final String username;
    private final String email;
    private final Collection<GrantedAuthority> authorities;

    public SecurityContext(Jwt jwt) {
        this.userId = jwt.getClaimAsString("sub");
        this.username = jwt.getClaimAsString("preferred_username");
        this.email = jwt.getClaimAsString("email");
        
        Collection<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null) {
            this.authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            this.authorities = Collections.emptyList();
        }
    }

    public boolean hasRole(String role) {
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }
}
