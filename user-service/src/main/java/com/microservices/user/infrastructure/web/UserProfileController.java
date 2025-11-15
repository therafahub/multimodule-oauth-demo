package com.microservices.user.infrastructure.web;

import com.microservices.common.dto.ApiResponse;
import com.microservices.user.application.dto.UserProfileDto;
import com.microservices.user.domain.service.UserProfileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Controlador REST para Perfiles de Usuario
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileUseCase userProfileUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDto>> createProfile(@RequestBody UserProfileDto request) {
        log.info("Creando perfil para usuario: {}", request.getUserId());

        var result = userProfileUseCase.createProfile(
            request.getUserId(),
            request.getBio(),
            request.getAvatarUrl()
        );

        return result.fold(
            error -> ResponseEntity.badRequest()
                .body(ApiResponse.<UserProfileDto>builder()
                    .code("ERROR")
                    .message(error)
                    .status(400)
                    .build()),
            profile -> ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(mapToDto(profile)))
        );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(@PathVariable Long userId) {
        log.info("Obteniendo perfil para usuario: {}", userId);

        var result = userProfileUseCase.getProfile(userId);

        return result.fold(
            error -> ResponseEntity.notFound().build(),
            profile -> ResponseEntity.ok(ApiResponse.ok(mapToDto(profile), "Profile found"))
        );
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileDto request) {
        log.info("Actualizando perfil para usuario: {}", userId);

        var result = userProfileUseCase.updateProfile(
            userId,
            request.getBio(),
            request.getAvatarUrl(),
            request.getPhoneNumber(),
            request.getCountry(),
            request.getCity()
        );

        return result.fold(
            error -> ResponseEntity.badRequest()
                .body(ApiResponse.<UserProfileDto>builder()
                    .code("ERROR")
                    .message(error)
                    .status(400)
                    .build()),
            profile -> ResponseEntity.ok(ApiResponse.ok(mapToDto(profile), "Profile updated"))
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<java.util.List<UserProfileDto>>> getAllProfiles() {
        var result = userProfileUseCase.getAllProfiles();

        return result.fold(
            error -> ResponseEntity.internalServerError().build(),
            profiles -> {
                var dtos = profiles.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.ok(dtos, "Profiles retrieved"));
            }
        );
    }

    private UserProfileDto mapToDto(com.microservices.user.domain.model.UserProfileEntity profile) {
        return UserProfileDto.builder()
            .id(profile.getId())
            .userId(profile.getUserId())
            .bio(profile.getBio())
            .avatarUrl(profile.getAvatarUrl())
            .phoneNumber(profile.getPhoneNumber())
            .country(profile.getCountry())
            .city(profile.getCity())
            .build();
    }
}
