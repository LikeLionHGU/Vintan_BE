package com.vintan.dto.response.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO for blind community post actions (create, update, delete).
 * The `isSuccess` field indicates success (1) or failure (0).
 */
@Getter
@AllArgsConstructor
public class CommunityBlindPostResponseDto {
    private int isSuccess;
}
