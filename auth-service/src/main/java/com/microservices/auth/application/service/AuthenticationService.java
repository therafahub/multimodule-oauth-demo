package com.microservices.auth.application.service;

import com.microservices.auth.domain.model.UserEntity;
import com.microservices.auth.domain.repository.UserRepositoryPort;
import com.microservices.auth.domain.service.AuthenticationUseCase;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementación de casos de uso de autenticación
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService implements AuthenticationUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Either<String, UserEntity> registerUser(String username, String email, String password, String firstName, String lastName) {
        log.info("Registrando nuevo usuario: {}", username);

        if (userRepository.existsByUsername(username)) {
            return Either.left("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            return Either.left("Email already exists");
        }

        try {
            UserEntity user = UserEntity.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .firstName(firstName)
                    .lastName(lastName)
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .roles(new HashSet<>(Set.of("USER")))
                    .build();

            UserEntity savedUser = userRepository.save(user);
            log.info("Usuario registrado exitosamente: {}", username);
            return Either.right(savedUser);
        } catch (Exception e) {
            log.error("Error al registrar usuario", e);
            return Either.left("Error registering user: " + e.getMessage());
        }
    }

    @Override
    public Either<String, UserEntity> authenticate(String username, String password) {
        log.info("Autenticando usuario: {}", username);

        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Either.left("Invalid credentials");
        }

        UserEntity user = userOpt.get();

        if (!user.isEnabled()) {
            return Either.left("User account is disabled");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Either.left("Invalid credentials");
        }

        log.info("Usuario autenticado: {}", username);
        return Either.right(user);
    }

    @Override
    public Either<String, UserEntity> getUserByUsername(String username) {
        log.debug("Buscando usuario: {}", username);

        var userOpt = userRepository.findByUsername(username);
        return userOpt.isEmpty() 
            ? Either.left("User not found")
            : Either.right(userOpt.get());
    }

    @Override
    public Either<String, UserEntity> getUserById(Long userId) {
        log.debug("Buscando usuario por ID: {}", userId);

        var userOpt = userRepository.findById(userId);
        return userOpt.isEmpty()
            ? Either.left("User not found")
            : Either.right(userOpt.get());
    }

    @Override
    public Either<String, List<UserEntity>> getAllUsers() {
        log.debug("Obteniendo todos los usuarios");
        try {
            return Either.right(userRepository.findAll());
        } catch (Exception e) {
            log.error("Error al obtener usuarios", e);
            return Either.left("Error retrieving users");
        }
    }

    @Override
    public Either<String, UserEntity> assignRoleToUser(Long userId, String role) {
        log.info("Asignando rol {} al usuario {}", role, userId);

        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Either.left("User not found");
        }

        UserEntity user = userOpt.get();
        user.addRole(role);
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updatedUser = userRepository.save(user);
        log.info("Rol asignado exitosamente");
        return Either.right(updatedUser);
    }

    @Override
    public Either<String, UserEntity> removeRoleFromUser(Long userId, String role) {
        log.info("Removiendo rol {} del usuario {}", role, userId);

        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Either.left("User not found");
        }

        UserEntity user = userOpt.get();
        user.removeRole(role);
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updatedUser = userRepository.save(user);
        log.info("Rol removido exitosamente");
        return Either.right(updatedUser);
    }
}
