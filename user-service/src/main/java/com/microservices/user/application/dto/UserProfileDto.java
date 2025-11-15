package com.microservices.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO para Perfil de Usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String bio;
    private String avatarUrl;
    private String phoneNumber;
    private String country;
    private String city;
}
