package com.microservices.user.domain.repository;

import io.vavr.control.Option;
import com.microservices.user.domain.model.UserProfileEntity;
import java.util.List;

/**
 * Puerto de salida para persistencia de perfiles (Hexagonal)
 */
public interface UserProfileRepositoryPort {

    Option<UserProfileEntity> findByUserId(Long userId);

    Option<UserProfileEntity> findById(Long id);

    UserProfileEntity save(UserProfileEntity profile);

    void delete(Long id);

    List<UserProfileEntity> findAll();
}
