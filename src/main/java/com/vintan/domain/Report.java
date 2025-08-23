package com.vintan.domain;

import com.vintan.domain.embedded.AccessibilityAnalysis;
import com.vintan.domain.embedded.FinalScore;
import com.vintan.domain.embedded.FloatingPopulationAnalysis;
import com.vintan.domain.embedded.OverallReview;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a business analysis report.
 * Stores various analytical scores, summaries, competitors, and metadata.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-generated

    private String address; // Street address of the report location
    private String category; // Business category

    @Lob
    private String startupSquareAnalysis; // Long text analysis of startup square

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "summary", column = @Column(name = "population_summary", length = 1000)),
            @AttributeOverride(name = "score", column = @Column(name = "population_score"))
    })
    private FloatingPopulationAnalysis floatingPopulationAnalysis; // Analysis of floating population

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "summary", column = @Column(name = "accessibility_summary", length = 1000)),
            @AttributeOverride(name = "score", column = @Column(name = "accessibility_score"))
    })
    private AccessibilityAnalysis accessibilityAnalysis; // Accessibility analysis

    @Embedded
    private OverallReview overallReview; // Overall review of the location

    // Average scores across various aspects
    private double averageCommunityScore;
    private double averageCleannessScore;
    private double averagePopulationScore;
    private double averageReachabilityScore;
    private double averageRentFeeScore;

    @Lob
    private String finalReportSummary; // Text summary of the final report

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "totalScore", column = @Column(name = "final_total_score")),
            @AttributeOverride(name = "competitionScore", column = @Column(name = "final_competition_score")),
            @AttributeOverride(name = "hinterlandScore", column = @Column(name = "final_hinterland_score")),
            @AttributeOverride(name = "accessibilityScore", column = @Column(name = "final_accessibility_score")),
            @AttributeOverride(name = "rentFeeScore", column = @Column(name = "final_rent_fee_score"))
    })
    private FinalScore finalScore; // Detailed final scoring breakdown

    @Builder.Default
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competitor> competitors = new ArrayList<>(); // Associated competitor data

    @Lob
    private String competitorSummary; // Summary of competitor analysis

    private String addressName; // Friendly or display name of the address
    private int postCount; // Number of related posts

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate; // Timestamp when the report was created

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Author of the report

    /**
     * Convenience method to add a competitor to the report.
     * Ensures bidirectional mapping is maintained.
     */
    public void addCompetitor(Competitor competitor) {
        this.competitors.add(competitor);
        competitor.setReport(this);
    }
}
