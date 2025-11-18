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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/validate")
    public ResponseEntity<?> validateCredentials(@RequestBody ValidateRequest request) {
        log.info("🔐 Validando credenciales para usuario: {}", request.getUsername());

        var result = authenticationUseCase.validateCredentials(
            request.getUsername(),
            request.getPassword()
        );

        return result.fold(
            error -> {
                log.warn("❌ Validación fallida para usuario: {} - Error: {}", request.getUsername(), error);
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("username", request.getUsername());
                response.put("roles", List.of());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            },
            user -> {
                log.info("✅ Validación exitosa para usuario: {}", request.getUsername());
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", user.getUsername());
                response.put("roles", user.getRoles());
                return ResponseEntity.ok(response);
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

    // DTO para validación
    public static class ValidateRequest {
        private String username;
        private String password;

        public ValidateRequest() {}

        public ValidateRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
