package com.vintan.dto.response.ai;

import com.vintan.embedded.FloatingPopulationAnalysis;
import jakarta.persistence.Lob;
import lombok.Getter;

@Getter
public class FloatingPopulationAnalysisDto {
    private String summary;
    private String weekdayAnalysis;
    private String weekendAnalysis;
    private String nearbyFacilities;
    private String ageGroup;
    private int score;

    public FloatingPopulationAnalysisDto(FloatingPopulationAnalysis analysis) {
        summary = analysis.getSummary();
        weekdayAnalysis = analysis.getWeekdayAnalysis();
        weekendAnalysis = analysis.getWeekendAnalysis();
        nearbyFacilities = analysis.getNearbyFacilities();
        ageGroup = analysis.getAgeGroup();
        score = analysis.getScore();
    }
}
