// AuthResponse.java
package com.coopcredit.creditapplicationservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;

    public AuthResponse(String token) {
        this.token = token;
    }

    public AuthResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
}
