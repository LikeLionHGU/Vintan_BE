package com.vintan.dto.response.ai.reportresponse;

import com.vintan.domain.embedded.AccessibilityAnalysis;
import lombok.Getter;

/**
 * DTO for returning accessibility analysis information in API responses.
 */
@Getter
public class AccessibilityAnalysisResponseDto {

    private String summary;          // General summary of accessibility
    private String parkingPrice;     // Information about parking and fees
    private String landmark;         // Nearby landmarks
    private String publicTransport;  // Details about public transport availability
    private String stationInfo;      // Subway or train station information
    private int score;               // Accessibility score

    /**
     * Constructor to map entity data to the response DTO.
     * @param analysis AccessibilityAnalysis entity
     */
    public AccessibilityAnalysisResponseDto(AccessibilityAnalysis analysis) {
        this.summary = analysis.getSummary();
        this.parkingPrice = analysis.getParking();
        this.landmark = analysis.getLandmark();
        this.publicTransport = analysis.getPublicTransport();
        this.stationInfo = analysis.getStationInfo();
        this.score = analysis.getScore();
    }
}
