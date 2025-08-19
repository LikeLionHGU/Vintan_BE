package com.vintan.dto.response.ai;

import com.vintan.embedded.AccessibilityAnalysis;
import lombok.Getter;

@Getter
public class AccessibilityAnalysisResponseDto {
    private String summary;
    private String parkingPrice;
    private String landmark;
    private String publicTransport;
    private String stationInfo;
    private int score;

    public AccessibilityAnalysisResponseDto(AccessibilityAnalysis analysis) {
        this.summary = analysis.getSummary();
        this.parkingPrice = analysis.getParking();
        this.landmark = analysis.getLandmark();
        this.publicTransport = analysis.getPublicTransport();
        this.stationInfo = analysis.getStationInfo();
        this.score = analysis.getScore();
    }
}
