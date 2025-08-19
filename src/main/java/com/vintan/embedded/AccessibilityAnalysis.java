package com.vintan.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessibilityAnalysis {
    @Lob
    private String summary;
    private String parking;
    private String landmark;
    private String publicTransport;
    private String stationInfo;
    private int score;
}
