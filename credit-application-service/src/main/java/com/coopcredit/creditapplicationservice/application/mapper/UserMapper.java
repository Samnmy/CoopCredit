package com.coopcredit.creditapplicationservice.application.mapper;

import com.coopcredit.creditapplicationservice.domain.model.User;
import com.coopcredit.creditapplicationservice.application.dto.RegisterRequest;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toDomain(RegisterRequest request);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
