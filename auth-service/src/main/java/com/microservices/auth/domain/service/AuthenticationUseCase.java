package com.microservices.auth.domain.service;

import com.microservices.auth.domain.model.UserEntity;
import io.vavr.control.Either;
import java.util.List;

/**
 * Puerto de entrada - Casos de uso de autenticación (Hexagonal)
 */
public interface AuthenticationUseCase {

    /**
     * Registra un nuevo usuario
     */
    Either<String, UserEntity> registerUser(String username, String email, String password, String firstName, String lastName);

    /**
     * Autentica un usuario
     */
    Either<String, UserEntity> authenticate(String username, String password);

    /**
     * Busca un usuario por username
     */
    Either<String, UserEntity> getUserByUsername(String username);

    /**
     * Busca un usuario por ID
     */
    Either<String, UserEntity> getUserById(Long userId);

    /**
     * Obtiene todos los usuarios (Admin)
     */
    Either<String, List<UserEntity>> getAllUsers();

    /**
     * Asigna un rol a un usuario
     */
    Either<String, UserEntity> assignRoleToUser(Long userId, String role);

    /**
     * Remueve un rol de un usuario
     */
    Either<String, UserEntity> removeRoleFromUser(Long userId, String role);
}
