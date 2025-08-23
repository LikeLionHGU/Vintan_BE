package com.vintan.dto.response.ai;

import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import lombok.Getter;

@Getter
public class GeneralOverviewGeminiDto {
    private CommunityAllReviewResponseDto communityAllReviewResponseDto;
    private String output;
    private int postCount;

    public GeneralOverviewGeminiDto(String output, CommunityAllReviewResponseDto communityAllReviewResponseDto, int postCount) {
        this.communityAllReviewResponseDto = communityAllReviewResponseDto;
        this.output = output;
        this.postCount = postCount;
    }
}
