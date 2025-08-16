package com.vintan.dto.response.ai;

import com.vintan.domain.Report;
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

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.address = report.getAddress();
        this.category = report.getCategory();
        this.finalScore = new com.vintan.dto.response.report.FinalScoreResponseDto(report.getFinalScore());
        this.competitors = report.getCompetitors().stream()
                .map(CompetitorResponseDto::new)
                .collect(Collectors.toList());
        this.competitorSummary = report.getCompetitorSummary();
    }
}