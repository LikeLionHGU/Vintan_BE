package com.vintan.domain.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Embeddable entity representing the floating population analysis.
 * Contains detailed textual analysis and a numeric score.
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloatingPopulationAnalysis {

    @Lob
    private String summary;           // Summary of the floating population analysis

    private String weekdayAnalysis;   // Analysis of population during weekdays
    private String weekendAnalysis;   // Analysis of population during weekends
    private String nearbyFacilities;  // Nearby facilities influencing population
    private String ageGroup;          // Age group distribution analysis

    private int score;                // Overall score for floating population
}
