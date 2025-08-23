package com.vintan.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for representing the result of a logout action.
 * isLogout = 1 → Logout successful
 * isLogout = 0 → Logout failed
 */
@Getter
@AllArgsConstructor
public class LogoutResponseDto {

    private int isLogout; // 1 if logout succeeded, 0 if failed
}
