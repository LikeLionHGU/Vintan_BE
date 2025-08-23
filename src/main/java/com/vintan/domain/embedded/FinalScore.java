package com.vintan.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Embeddable entity representing the final scoring summary for a report.
 * Includes individual category scores, total score, and a textual summary.
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalScore {

    private int floatingPopulationScore;   // Score for floating population
    private int accessibilityScore;        // Score for accessibility
    private int competitionScore;          // Score for competition
    private int overallReviewScore;        // Score from overall review

    private int totalScore;                // Total aggregated score

    private String summary;                // Text summary of the final evaluation
}
