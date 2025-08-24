package com.vintan.dto.response.ai.reportresponse;

import com.vintan.domain.embedded.FloatingPopulationAnalysis;
import lombok.Getter;

/**
 * DTO for returning floating population analysis in API responses.
 * Maps the FloatingPopulationAnalysis embedded entity to a simplified response object.
 */
@Getter
public class FloatingPopulationAnalysisDto {

    private String summary;           // Summary of floating population analysis
    private String weekdayAnalysis;   // Analysis for weekdays
    private String weekendAnalysis;   // Analysis for weekends
    private String nearbyFacilities;  // Nearby facilities affecting floating population
    private String ageGroup;          // Age group distribution
    private int score;                // Score based on floating population analysis

    /**
     * Constructor that maps a FloatingPopulationAnalysis entity to this DTO.
     * @param analysis the FloatingPopulationAnalysis entity
     */
    public FloatingPopulationAnalysisDto(FloatingPopulationAnalysis analysis) {
        this.summary = analysis.getSummary();
        this.weekdayAnalysis = analysis.getWeekdayAnalysis();
        this.weekendAnalysis = analysis.getWeekendAnalysis();
        this.nearbyFacilities = analysis.getNearbyFacilities();
        this.ageGroup = analysis.getAgeGroup();
        this.score = analysis.getScore();
    }
}
