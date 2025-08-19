package com.vintan.dto.response.ai;

import com.vintan.embedded.OverallReview;
import jakarta.persistence.Lob;
import lombok.Getter;

@Getter
public class GeneralOverviewReportDto {
    private String summary;

    private String positive;

    private String negative;

    private int score;

    private double averageCommunityScore;
    private double averageCleannessScore;
    private double averagePopulationScore;
    private double averageReachabilityScore;
    private double averageRentFeeScore;

    public GeneralOverviewReportDto(
            OverallReview overallReview,
            double averageCommunityScore,
            double averageCleannessScore,
            double averagePopulationScore,
            double averageRentFeeScore,
            double averageReachabilityScore
    )
    {
        summary = overallReview.getReview_summary();
        positive = overallReview.getPositive();
        negative = overallReview.getNegative();
        score = overallReview.getReview_score();
        this.averageCommunityScore = averageCommunityScore;
        this.averageCleannessScore = averageCleannessScore;
        this.averagePopulationScore = averagePopulationScore;
        this.averageRentFeeScore = averageRentFeeScore;
        this.averageReachabilityScore = averageReachabilityScore;
    }
}
