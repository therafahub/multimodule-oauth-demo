package com.microservices.user.application.service;

import com.microservices.user.domain.model.UserProfileEntity;
import com.microservices.user.domain.repository.UserProfileRepositoryPort;
import com.microservices.user.domain.service.UserProfileUseCase;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación de casos de uso de perfil
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService implements UserProfileUseCase {

    private final UserProfileRepositoryPort userProfileRepository;

    @Override
    public Either<String, UserProfileEntity> createProfile(Long userId, String bio, String avatarUrl) {
        log.info("Creando perfil para usuario: {}", userId);

        try {
            UserProfileEntity profile = UserProfileEntity.builder()
                    .userId(userId)
                    .bio(bio)
                    .avatarUrl(avatarUrl)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            UserProfileEntity saved = userProfileRepository.save(profile);
            log.info("Perfil creado exitosamente para usuario: {}", userId);
            return Either.right(saved);
        } catch (Exception e) {
            log.error("Error creando perfil", e);
            return Either.left("Error creating profile: " + e.getMessage());
        }
    }

    @Override
    public Either<String, UserProfileEntity> getProfile(Long userId) {
        log.debug("Obteniendo perfil para usuario: {}", userId);

        var profileOpt = userProfileRepository.findByUserId(userId);
        return profileOpt.isEmpty()
            ? Either.left("Profile not found")
            : Either.right(profileOpt.get());
    }

    @Override
    public Either<String, UserProfileEntity> updateProfile(Long userId, String bio, String avatarUrl, String phoneNumber, String country, String city) {
        log.info("Actualizando perfil para usuario: {}", userId);

        var profileOpt = userProfileRepository.findByUserId(userId);
        if (profileOpt.isEmpty()) {
            return Either.left("Profile not found");
        }

        try {
            UserProfileEntity profile = profileOpt.get();
            profile.setBio(bio);
            profile.setAvatarUrl(avatarUrl);
            profile.setPhoneNumber(phoneNumber);
            profile.setCountry(country);
            profile.setCity(city);
            profile.setUpdatedAt(LocalDateTime.now());

            UserProfileEntity updated = userProfileRepository.save(profile);
            log.info("Perfil actualizado exitosamente para usuario: {}", userId);
            return Either.right(updated);
        } catch (Exception e) {
            log.error("Error actualizando perfil", e);
            return Either.left("Error updating profile: " + e.getMessage());
        }
    }

    @Override
    public Either<String, Void> deleteProfile(Long userId) {
        log.info("Eliminando perfil para usuario: {}", userId);

        try {
            var profileOpt = userProfileRepository.findByUserId(userId);
            if (profileOpt.isEmpty()) {
                return Either.left("Profile not found");
            }

            userProfileRepository.delete(profileOpt.get().getId());
            log.info("Perfil eliminado exitosamente para usuario: {}", userId);
            return Either.right(null);
        } catch (Exception e) {
            log.error("Error eliminando perfil", e);
            return Either.left("Error deleting profile");
        }
    }

    @Override
    public Either<String, List<UserProfileEntity>> getAllProfiles() {
        log.debug("Obteniendo todos los perfiles");
        try {
            return Either.right(userProfileRepository.findAll());
        } catch (Exception e) {
            log.error("Error obteniendo perfiles", e);
            return Either.left("Error retrieving profiles");
        }
    }
}
