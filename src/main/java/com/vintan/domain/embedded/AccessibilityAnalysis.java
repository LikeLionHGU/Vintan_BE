package com.vintan.domain.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Embeddable entity representing accessibility analysis of a location.
 * Stores various accessibility factors and an overall score.
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessibilityAnalysis {

    @Lob
    private String summary;        // Detailed textual summary of accessibility

    private String parking;        // Information about nearby parking availability
    private String landmark;       // Proximity to landmarks
    private String publicTransport;// Info on public transportation options
    private String stationInfo;    // Details about nearby stations (subway/train)

    private int score;             // Overall accessibility score
}
