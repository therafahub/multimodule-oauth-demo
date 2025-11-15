package com.microservices.auth.infrastructure.persistence;

import com.microservices.auth.domain.model.UserEntity;
import com.microservices.auth.domain.repository.UserRepositoryPort;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptador de persistencia - Implementa el puerto UserRepositoryPort (Hexagonal)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "users", key = "#username")
    public Option<UserEntity> findByUsername(String username) {
        log.debug("Buscando usuario en BD: {}", username);
        return Option.ofOptional(
            jpaRepository.findByUsername(username)
                .map(userMapper::toDomain)
        );
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public Option<UserEntity> findByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);
        return Option.ofOptional(
            jpaRepository.findByEmail(email)
                .map(userMapper::toDomain)
        );
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public Option<UserEntity> findById(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        return Option.ofOptional(
            jpaRepository.findById(id)
                .map(userMapper::toDomain)
        );
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserEntity save(UserEntity user) {
        log.debug("Guardando usuario: {}", user.getUsername());
        UserJpaEntity jpaEntity = userMapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(jpaEntity);
        return userMapper.toDomain(saved);
    }

    @Override
    public List<UserEntity> findAll() {
        log.debug("Obteniendo todos los usuarios");
        return jpaRepository.findAll()
            .stream()
            .map(userMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }
}
