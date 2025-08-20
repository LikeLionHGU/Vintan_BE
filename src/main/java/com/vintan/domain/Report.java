package com.vintan.domain;

import com.vintan.embedded.AccessibilityAnalysis;
import com.vintan.embedded.FinalScore;
import com.vintan.embedded.FloatingPopulationAnalysis;
import com.vintan.embedded.OverallReview;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long id;

    private String address;
    private String category;

    @Lob
    private String startupSquareAnalysis;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "summary", column = @Column(name = "population_summary", length = 1000)),
            @AttributeOverride(name = "score", column = @Column(name = "population_score"))
    })
    private FloatingPopulationAnalysis floatingPopulationAnalysis;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "summary", column = @Column(name = "accessibility_summary", length = 1000)),
            @AttributeOverride(name = "score", column = @Column(name = "accessibility_score"))
    })
    private AccessibilityAnalysis accessibilityAnalysis;

    @Embedded
    private OverallReview overallReview;

    private double averageCommunityScore;
    private double averageCleannessScore;
    private double averagePopulationScore;
    private double averageReachabilityScore;
    private double averageRentFeeScore;

    @Lob
    private String finalReportSummary;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "totalScore", column = @Column(name = "final_total_score")),
            @AttributeOverride(name = "competitionScore", column = @Column(name = "final_competition_score")),
            @AttributeOverride(name = "hinterlandScore", column = @Column(name = "final_hinterland_score")),
            @AttributeOverride(name = "accessibilityScore", column = @Column(name = "final_accessibility_score")),
            @AttributeOverride(name = "rentFeeScore", column = @Column(name = "final_rent_fee_score"))
    })
    private FinalScore finalScore;

    @Builder.Default
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competitor> competitors = new ArrayList<>();

    @Lob
    private String competitorSummary;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    public void addCompetitor(Competitor competitor) {
        this.competitors.add(competitor);
        competitor.setReport(this);
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}