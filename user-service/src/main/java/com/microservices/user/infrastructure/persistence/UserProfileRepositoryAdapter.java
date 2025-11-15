package com.microservices.user.infrastructure.persistence;

import com.microservices.user.domain.model.UserProfileEntity;
import com.microservices.user.domain.repository.UserProfileRepositoryPort;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptador de persistencia para UserProfile
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {

    private final UserProfileJpaRepository jpaRepository;
    private final UserProfileMapper mapper;

    @Override
    @Cacheable(value = "userProfiles", key = "#userId")
    public Option<UserProfileEntity> findByUserId(Long userId) {
        log.debug("Buscando perfil por userId: {}", userId);
        return Option.ofOptional(
            jpaRepository.findByUserId(userId)
                .map(mapper::toDomain)
        );
    }

    @Override
    @Cacheable(value = "userProfiles", key = "#id")
    public Option<UserProfileEntity> findById(Long id) {
        log.debug("Buscando perfil por ID: {}", id);
        return Option.ofOptional(
            jpaRepository.findById(id)
                .map(mapper::toDomain)
        );
    }

    @Override
    @CacheEvict(value = "userProfiles", allEntries = true)
    public UserProfileEntity save(UserProfileEntity profile) {
        log.debug("Guardando perfil para usuario: {}", profile.getUserId());
        UserProfileJpaEntity jpaEntity = mapper.toJpa(profile);
        UserProfileJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    @CacheEvict(value = "userProfiles", allEntries = true)
    public void delete(Long id) {
        log.debug("Eliminando perfil: {}", id);
        jpaRepository.deleteById(id);
    }

    @Override
    public List<UserProfileEntity> findAll() {
        log.debug("Obteniendo todos los perfiles");
        return jpaRepository.findAll()
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}
