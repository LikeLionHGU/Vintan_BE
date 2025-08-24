package com.vintan.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO representing the result of a user registration attempt.
 * isRegistered = 1 → Registration successful
 * isRegistered = 0 → Registration failed
 */
@Getter
@AllArgsConstructor
public class RegisterResponseDto {

    private int isRegistered; // 1 if registration succeeded, 0 if failed
}
