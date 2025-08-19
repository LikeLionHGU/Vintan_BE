package com.vintan.dto.response.ai;

import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import lombok.Getter;

@Getter
public class GeneralOverviewGeminiDto {
    private CommunityAllReviewResponseDto communityAllReviewResponseDto;
    private String output;

    public GeneralOverviewGeminiDto(String output, CommunityAllReviewResponseDto communityAllReviewResponseDto) {
        this.communityAllReviewResponseDto = communityAllReviewResponseDto;
        this.output = output;
    }
}
