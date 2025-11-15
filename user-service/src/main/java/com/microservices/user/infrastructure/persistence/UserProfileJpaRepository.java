package com.microservices.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository
 */
@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfileJpaEntity, Long> {

    Optional<UserProfileJpaEntity> findByUserId(Long userId);
}
