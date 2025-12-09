package com.coopcredit.creditapplicationservice.infrastructure.persistence.adapter;

import com.coopcredit.creditapplicationservice.domain.model.User;
import com.coopcredit.creditapplicationservice.domain.port.UserPort;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.entity.UserEntity;
import com.coopcredit.creditapplicationservice.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEnabled(user.getEnabled());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());

        // roles dominio → roles persistencia (mismo enum)
        entity.setRoles(new HashSet<>(user.getRoles()));

        entity.setAffiliateId(user.getAffiliateId());
        return entity;
    }

    @Override
    public User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .roles(new HashSet<>(entity.getRoles()))
                .affiliateId(entity.getAffiliateId())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}