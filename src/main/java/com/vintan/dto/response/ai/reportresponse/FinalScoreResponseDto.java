package com.vintan.dto.response.ai.reportresponse;

import com.vintan.domain.embedded.FinalScore;
import lombok.Getter;

/**
 * DTO for returning the final score analysis in API responses.
 * Maps the FinalScore embedded entity to a simple response object.
 */
@Getter
public class FinalScoreResponseDto {

    private int floatingPopulationScore; // Score for floating population analysis
    private int accessibilityScore;      // Score for accessibility analysis
    private int competitionScore;        // Score for competition evaluation
    private int overallReviewScore;      // Score from overall review
    private int totalScore;              // Total score combining all metrics

    /**
     * Constructor that maps a FinalScore entity to this DTO.
     * @param finalScore the FinalScore entity
     */
    public FinalScoreResponseDto(FinalScore finalScore) {
        this.floatingPopulationScore = finalScore.getFloatingPopulationScore();
        this.accessibilityScore = finalScore.getAccessibilityScore();
        this.competitionScore = finalScore.getCompetitionScore();
        this.overallReviewScore = finalScore.getOverallReviewScore();
        this.totalScore = finalScore.getTotalScore();
    }
}
