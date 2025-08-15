package com.vintan.dto.response.community;

import lombok.Getter;

@Getter
public class CommunityDetailResponseDto {
    private double totalRate;
    private CommunityBlindDetailResponseDto blind;

    public CommunityDetailResponseDto(double totalRate, CommunityBlindDetailResponseDto blind) {
        this.totalRate = totalRate;
        this.blind = blind;
    }
}
