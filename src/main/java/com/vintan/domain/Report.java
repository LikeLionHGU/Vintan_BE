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
    @AttributeOverrides({
            @AttributeOverride(name = "categoryRate.cleanness", column = @Column(name = "overall_cleanness_score")),
            @AttributeOverride(name = "categoryRate.people", column = @Column(name = "overall_people_score")),
            @AttributeOverride(name = "categoryRate.reach", column = @Column(name = "overall_reach_score")),
            @AttributeOverride(name = "categoryRate.rentFee", column = @Column(name = "overall_rent_fee_score")),
            // OverallReview 클래스의 필드 이름 변경을 여기에 반영
            @AttributeOverride(name = "scoreReview", column = @Column(name = "overall_review_score"))
    })
    private OverallReview overallReview;

    @Embedded
    // 향후 충돌 방지를 위해 FinalScore의 컬럼명도 명시적으로 지정
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