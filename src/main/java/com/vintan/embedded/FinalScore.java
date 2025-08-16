package com.vintan.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalScore {

    private int floatingPopulationScore;
    private int accessibilityScore;
    private int competitionScore;
    private int overallReviewScore;

    private int totalScore;

    private String summary;
}
