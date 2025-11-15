package com.microservices.user.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad de dominio para Perfil de Usuario
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserProfileEntity {

    private Long id;
    private Long userId;
    private String bio;
    private String avatarUrl;
    private String phoneNumber;
    private String country;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
