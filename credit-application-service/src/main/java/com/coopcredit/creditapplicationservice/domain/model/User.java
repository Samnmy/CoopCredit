package com.coopcredit.creditapplicationservice.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private String username;
    private String password;
    private String email;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    private Long affiliateId;
}
