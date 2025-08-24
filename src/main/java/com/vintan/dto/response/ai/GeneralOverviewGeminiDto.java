package com.vintan.dto.response.ai;

import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import lombok.Getter;

/**
 * DTO for returning a general overview from the Gemini AI response.
 * Includes community reviews, AI-generated output, and total post count.
 */
@Getter
public class GeneralOverviewGeminiDto {

    private CommunityAllReviewResponseDto communityAllReviewResponseDto; // Aggregated community review data
    private String output;  // AI-generated summary or analysis text
    private int postCount;  // Total number of posts considered

    /**
     * Constructor to initialize all fields.
     *
     * @param output AI-generated summary or analysis
     * @param communityAllReviewResponseDto Aggregated community reviews
     * @param postCount Total number of posts
     */
    public GeneralOverviewGeminiDto(String output, CommunityAllReviewResponseDto communityAllReviewResponseDto, int postCount) {
        this.communityAllReviewResponseDto = communityAllReviewResponseDto;
        this.output = output;
        this.postCount = postCount;
    }
}
