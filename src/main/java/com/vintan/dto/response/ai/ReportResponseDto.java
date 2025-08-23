package com.vintan.dto.response.ai;

import com.vintan.domain.Report;
import com.vintan.dto.response.ai.reportresponse.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for returning detailed information about a report.
 * Maps the domain Report entity to a structured API response.
 */
@Getter
public class ReportResponseDto {

    private Long id;                                   // Report ID
    private String address;                            // Address of the location
    private String category;                           // Category code of the report
    private FinalScoreResponseDto finalScore;         // Embedded final score information
    private List<CompetitorResponseDto> competitors;  // List of competitors
    private String competitorSummary;                 // Summary of competitors
    private AccessibilityAnalysisResponseDto accessibilityAnalysis; // Accessibility analysis details
    private FloatingPopulationAnalysisDto floatingPopulationAnalysis; // Floating population analysis details
    private GeneralOverviewReportDto generalOverviewReport; // Overall review summary and scores
    private String finalReportReviewSummary;          // Final summary text
    private String addressName;                        // Human-readable address
    private int postCount;                             // Count of related posts

    /**
     * Constructor that maps a Report entity to this response DTO.
     * @param report the Report entity from the database
     */
    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.address = report.getAddress();
        this.category = report.getCategory();
        this.finalScore = new FinalScoreResponseDto(report.getFinalScore());
        this.accessibilityAnalysis = new AccessibilityAnalysisResponseDto(report.getAccessibilityAnalysis());
        this.floatingPopulationAnalysis = new FloatingPopulationAnalysisDto(report.getFloatingPopulationAnalysis());
        this.generalOverviewReport = new GeneralOverviewReportDto(
                report.getOverallReview(),
                report.getAverageCommunityScore(),
                report.getAverageCleannessScore(),
                report.getAveragePopulationScore(),
                report.getAverageRentFeeScore(),
                report.getAverageReachabilityScore()
        );
        this.finalReportReviewSummary = report.getFinalReportSummary();
        this.competitors = report.getCompetitors().stream()
                .map(CompetitorResponseDto::new)
                .collect(Collectors.toList());
        this.competitorSummary = report.getCompetitorSummary();
        this.addressName = report.getAddressName();
        this.postCount = report.getPostCount();
    }
}
