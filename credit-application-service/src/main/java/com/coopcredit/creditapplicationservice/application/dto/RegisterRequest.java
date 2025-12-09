// RegisterRequest.java
package com.coopcredit.creditapplicationservice.application.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private Set<String> roles;
    private Long affiliateId;
}