package com.microservices.auth.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad de dominio para Usuario
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "roles"})
public class UserEntity {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> roles = new HashSet<>();

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }

    public boolean hasRole(String role) {
        return this.roles.contains(role);
    }
}
