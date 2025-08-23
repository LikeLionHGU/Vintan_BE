package com.vintan.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for checking if a user ID is duplicated.
 * isDuplicated = 1 → ID already exists
 * isDuplicated = 0 → ID is available
 */
@Getter
@AllArgsConstructor
public class DuplicatedCheckResponseDto {

    private int isDuplicated; // 1 if duplicated, 0 if available
}
