package com.vintan.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
public class OverallReview {
    private double totalAverageScore;

    @Embedded
    private CategoryRate categoryRate;

    private String keywords;
    @Lob
    private String strengthsSummary;
    @Lob
    private String weaknessesSummary;

    private int reviewScore;
}
