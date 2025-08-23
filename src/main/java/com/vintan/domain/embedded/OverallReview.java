package com.vintan.domain.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Embeddable entity representing an overall review.
 * Stores textual analysis of positive and negative aspects, along with a numeric review score.
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverallReview {

    @Lob
    private String summary;       // Summary of the overall review

    @Lob
    private String positive;      // Text describing positive points

    @Lob
    private String negative;      // Text describing negative points

    private int reviewScore;      // Numeric score for the overall review
}
