package com.vintan.dto.response.community;

import lombok.Getter;

/**
 * DTO representing the detailed view of a community post.
 * Contains the overall rating and details of the blind community post.
 */
@Getter
public class CommunityDetailResponseDto {
    private double totalRate;
    private CommunityBlindDetailResponseDto blind;

    public CommunityDetailResponseDto(double totalRate, CommunityBlindDetailResponseDto blind) {
        this.totalRate = totalRate;
        this.blind = blind;
    }
}
