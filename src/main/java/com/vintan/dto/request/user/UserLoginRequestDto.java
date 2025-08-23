package com.vintan.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for user login request.
 * Contains the user ID and password.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequestDto {

    @NotBlank(message = "User ID is required")
    private String id; // User identifier

    @NotBlank(message = "Password is required")
    private String password; // User password
}
