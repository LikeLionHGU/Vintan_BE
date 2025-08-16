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
public class FloatingPopulationAnalysis {
    @Lob
    private String summary;
    private String weekdayAnalysis;
    private String weekendAnalysis;
    private String nearbyFacilities;
    private String ageGroup;
    private int score;
}
