package com.vintan.dto.response.ai.reportresponse;

import com.vintan.domain.embedded.OverallReview;
import lombok.Getter;

/**
 * DTO representing a general overview of a report.
 * Includes overall review details and average scores across multiple categories.
 */
@Getter
public class GeneralOverviewReportDto {

    private String summary;          // Summary of the overall review
    private String positive;         // Positive points highlighted in the review
    private String negative;         // Negative points highlighted in the review
    private int score;               // Overall review score

    private double averageCommunityScore;    // Average score for community aspects
    private double averageCleannessScore;    // Average score for cleanness
    private double averagePopulationScore;   // Average score for floating population
    private double averageReachabilityScore; // Average score for accessibility/reach
    private double averageRentFeeScore;      // Average score for rent/lease fee

    /**
     * Constructor to initialize all fields from OverallReview and average scores.
     *
     * @param overallReview Overall review entity containing summary, positive, negative, and score
     * @param averageCommunityScore Average score for community
     * @param averageCleannessScore Average score for cleanness
     * @param averagePopulationScore Average score for floating population
     * @param averageRentFeeScore Average score for rent fee
     * @param averageReachabilityScore Average score for accessibility/reach
     */
    public GeneralOverviewReportDto(
            OverallReview overallReview,
            double averageCommunityScore,
            double averageCleannessScore,
            double averagePopulationScore,
            double averageRentFeeScore,
            double averageReachabilityScore
    ) {
        this.summary = overallReview.getSummary();
        this.positive = overallReview.getPositive();
        this.negative = overallReview.getNegative();
        this.score = overallReview.getReviewScore();
        this.averageCommunityScore = averageCommunityScore;
        this.averageCleannessScore = averageCleannessScore;
        this.averagePopulationScore = averagePopulationScore;
        this.averageRentFeeScore = averageRentFeeScore;
        this.averageReachabilityScore = averageReachabilityScore;
    }
}
