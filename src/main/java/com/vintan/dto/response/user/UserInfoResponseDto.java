package com.vintan.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for returning basic user info in session context.
 * Indicates whether the user is logged in and whether they are a business user.
 */
@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    private final int isLogin;      // 1 if user is logged in, 0 otherwise
    private final int isBusiness;   // 1 if user has a business number, 0 otherwise
    private final String name;

}
