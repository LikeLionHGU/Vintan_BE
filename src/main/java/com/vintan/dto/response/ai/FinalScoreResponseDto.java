package com.vintan.dto.response.report;

import com.vintan.embedded.FinalScore;
import lombok.Getter;

@Getter
public class FinalScoreResponseDto {
    private int floatingPopulationScore;
    private int accessibilityScore;
    private int competitionScore;
    private int overallReviewScore;
    private int totalScore;

    public FinalScoreResponseDto(FinalScore finalScore) {
        this.floatingPopulationScore = finalScore.getFloatingPopulationScore();
        this.accessibilityScore = finalScore.getAccessibilityScore();
        this.competitionScore = finalScore.getCompetitionScore();
        this.overallReviewScore = finalScore.getOverallReviewScore();
        this.totalScore = finalScore.getTotalScore();
    }
}

