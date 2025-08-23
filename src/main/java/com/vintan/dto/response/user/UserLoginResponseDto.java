package com.vintan.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for returning the result of a login attempt.
 * Indicates whether login was successful.
 */
@Getter
@AllArgsConstructor
public class UserLoginResponseDto {

    private final int isLogin;  // 1 if login successful, 0 otherwise

}
