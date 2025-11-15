package com.microservices.auth.domain.repository;

import io.vavr.control.Option;
import java.util.List;

/**
 * Puerto de salida para persistencia de usuarios (Hexagonal)
 */
public interface UserRepositoryPort {

    /**
     * Busca un usuario por nombre de usuario
     */
    Option<com.microservices.auth.domain.model.UserEntity> findByUsername(String username);

    /**
     * Busca un usuario por correo electrónico
     */
    Option<com.microservices.auth.domain.model.UserEntity> findByEmail(String email);

    /**
     * Busca un usuario por ID
     */
    Option<com.microservices.auth.domain.model.UserEntity> findById(Long id);

    /**
     * Guarda un usuario
     */
    com.microservices.auth.domain.model.UserEntity save(com.microservices.auth.domain.model.UserEntity user);

    /**
     * Obtiene todos los usuarios
     */
    List<com.microservices.auth.domain.model.UserEntity> findAll();

    /**
     * Verifica si existe un usuario por email
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario por username
     */
    boolean existsByUsername(String username);
}
