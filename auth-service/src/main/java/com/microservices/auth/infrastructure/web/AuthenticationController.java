package com.microservices.auth.infrastructure.web;

import com.microservices.common.dto.ApiResponse;
import com.microservices.auth.application.dto.RegisterRequest;
import com.microservices.auth.application.dto.UserDto;
import com.microservices.auth.domain.service.AuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Controlador REST - Puerto de entrada HTTP
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody RegisterRequest request) {
        log.info("Solicitud de registro: {}", request.getUsername());

        var result = authenticationUseCase.registerUser(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getFirstName(),
            request.getLastName()
        );

        return result.fold(
            error -> {
                log.warn("Error en registro: {}", error);
                return ResponseEntity.badRequest()
                    .body(ApiResponse.<UserDto>builder()
                        .code("ERROR")
                        .message(error)
                        .status(400)
                        .build());
            },
            user -> {
                UserDto dto = mapToDto(user);
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(dto));
            }
        );
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String username) {
        log.info("Buscando usuario: {}", username);

        var result = authenticationUseCase.getUserByUsername(username);

        return result.fold(
            error -> ResponseEntity.notFound().build(),
            user -> ResponseEntity.ok(ApiResponse.ok(mapToDto(user), "User found"))
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<java.util.List<UserDto>>> getAllUsers() {
        var result = authenticationUseCase.getAllUsers();

        return result.fold(
            error -> ResponseEntity.internalServerError().build(),
            users -> {
                var dtos = users.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.ok(dtos, "Users retrieved"));
            }
        );
    }

    @PostMapping("/users/{userId}/roles/{role}")
    public ResponseEntity<ApiResponse<UserDto>> assignRole(
            @PathVariable Long userId,
            @PathVariable String role) {
        log.info("Asignando rol {} a usuario {}", role, userId);

        var result = authenticationUseCase.assignRoleToUser(userId, role);

        return result.fold(
            error -> ResponseEntity.badRequest()
                .body(ApiResponse.<UserDto>builder()
                    .code("ERROR")
                    .message(error)
                    .status(400)
                    .build()),
            user -> ResponseEntity.ok(ApiResponse.ok(mapToDto(user), "Role assigned"))
        );
    }

    private UserDto mapToDto(com.microservices.auth.domain.model.UserEntity user) {
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .enabled(user.isEnabled())
            .roles(user.getRoles())
            .build();
    }
}
