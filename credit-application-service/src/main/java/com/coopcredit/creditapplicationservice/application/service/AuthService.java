package com.coopcredit.creditapplicationservice.application.service;

import com.coopcredit.creditapplicationservice.application.dto.AuthRequest;
import com.coopcredit.creditapplicationservice.application.dto.AuthResponse;
import com.coopcredit.creditapplicationservice.application.dto.RegisterRequest;
import com.coopcredit.creditapplicationservice.domain.model.Role;
import com.coopcredit.creditapplicationservice.domain.model.User;
import com.coopcredit.creditapplicationservice.domain.port.UserPort;
import com.coopcredit.creditapplicationservice.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        User user = userPort.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User logged in successfully: {}", request.getUsername());
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }

    public User register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userPort.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userPort.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .affiliateId(request.getAffiliateId())
                .enabled(true)
                .build();

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            roles.add(Role.ROLE_AFFILIATE);
        } else {
            roles = request.getRoles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
        }
        user.setRoles(roles);

        User savedUser = userPort.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }
}