package com.microservices.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO para solicitud de registro
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
