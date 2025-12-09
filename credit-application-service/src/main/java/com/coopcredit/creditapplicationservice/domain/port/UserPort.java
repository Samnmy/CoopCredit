package com.coopcredit.creditapplicationservice.domain.port;

import com.coopcredit.creditapplicationservice.domain.model.User;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserPort {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User toDomain(UserEntity entity);
}