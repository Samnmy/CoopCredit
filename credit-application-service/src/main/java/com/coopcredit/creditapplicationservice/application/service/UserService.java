package com.coopcredit.creditapplicationservice.application.service;

import com.coopcredit.creditapplicationservice.domain.model.User;
import com.coopcredit.creditapplicationservice.domain.port.UserPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserPort userPort;

    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        User user = userPort.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            log.error("User is disabled: {}", username);
            throw new UsernameNotFoundException("User is disabled: " + username);
        }

        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        log.debug("User loaded successfully: {}", username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),   // enabled
                true,                // accountNonExpired
                true,                // credentialsNonExpired
                true,                // accountNonLocked
                authorities
        );
    }
}
