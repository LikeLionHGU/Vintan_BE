package com.vintan.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for user registration request.
 * Contains user credentials and optional business information.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequestDto {

    @NotBlank(message = "User ID is required")
    private String id; // Unique user identifier

    @NotBlank(message = "Name is required")
    private String name; // User's full name

    @NotBlank(message = "Password is required")
    // You can add @Size(min = 8, message = "Password must be at least 8 characters") if needed
    private String password; // User's password

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email; // User's email

    private int businessNumber; // Optional business number
}
