package com.microservices.auth.infrastructure.persistence;

import com.microservices.auth.domain.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper entre capas usando MapStruct
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(source = ".", target = ".")
    UserEntity toDomain(UserJpaEntity jpaEntity);

    @Mapping(source = ".", target = ".")
    UserJpaEntity toJpa(UserEntity domainEntity);
}
