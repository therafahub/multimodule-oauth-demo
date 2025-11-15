package com.microservices.user.infrastructure.persistence;

import com.microservices.user.domain.model.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper para UserProfile
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    UserProfileEntity toDomain(UserProfileJpaEntity jpaEntity);

    UserProfileJpaEntity toJpa(UserProfileEntity domainEntity);
}
