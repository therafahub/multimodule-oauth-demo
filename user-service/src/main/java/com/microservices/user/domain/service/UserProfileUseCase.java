package com.microservices.user.domain.service;

import com.microservices.user.domain.model.UserProfileEntity;
import io.vavr.control.Either;
import java.util.List;

/**
 * Puerto de entrada - Casos de uso de perfil (Hexagonal)
 */
public interface UserProfileUseCase {

    Either<String, UserProfileEntity> createProfile(Long userId, String bio, String avatarUrl);

    Either<String, UserProfileEntity> getProfile(Long userId);

    Either<String, UserProfileEntity> updateProfile(Long userId, String bio, String avatarUrl, String phoneNumber, String country, String city);

    Either<String, Void> deleteProfile(Long userId);

    Either<String, List<UserProfileEntity>> getAllProfiles();
}
