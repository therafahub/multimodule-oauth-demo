package com.microservices.auth.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsername(String username);

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
