package com.microservices.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO para respuesta de usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private boolean enabled;

    private Set<String> roles;
}
