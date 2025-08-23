package com.vintan.dto.response.ai;

import com.vintan.domain.Report;
import com.vintan.embedded.FloatingPopulationAnalysis;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReportResponseDto {
    private Long id;
    private String address;
    private String category;
    private com.vintan.dto.response.report.FinalScoreResponseDto finalScore;
    private List<CompetitorResponseDto> competitors;
    private String competitorSummary;
    private AccessibilityAnalysisResponseDto accessibilityAnalysis;
    private FloatingPopulationAnalysisDto floatingPopulationAnalysis;
    private GeneralOverviewReportDto generalOverviewReport;
    private String finalReportReviewSummary;
    private String addressName;
    private int postCount;

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.address = report.getAddress();
        this.category = report.getCategory();
        this.finalScore = new com.vintan.dto.response.report.FinalScoreResponseDto(report.getFinalScore());
        this.accessibilityAnalysis = new AccessibilityAnalysisResponseDto(report.getAccessibilityAnalysis());
        this.floatingPopulationAnalysis = new FloatingPopulationAnalysisDto(report.getFloatingPopulationAnalysis());
        this.generalOverviewReport = new GeneralOverviewReportDto(report.getOverallReview(), report.getAverageCommunityScore(), report.getAverageCleannessScore(), report.getAveragePopulationScore(), report.getAverageRentFeeScore(), report.getAverageReachabilityScore());
        this.finalReportReviewSummary = report.getFinalReportSummary();
        this.competitors = report.getCompetitors().stream()
                .map(CompetitorResponseDto::new)
                .collect(Collectors.toList());
        this.competitorSummary = report.getCompetitorSummary();
        this.addressName = report.getAddressName();
        this.postCount = report.getPostCount();
    }
}