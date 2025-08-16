package com.vintan.service;

import com.vintan.component.GeminiApiClient;
import com.vintan.component.KakaoMapClient;
import com.vintan.domain.Report;
import com.vintan.dto.aiReport.CompanyDto;
import com.vintan.dto.response.ai.AiReportResponseDto;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import com.vintan.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final KakaoMapClient kakaoMapClient;
    private final GeminiApiClient geminiApiClient;

    @Transactional
    public AiReportResponseDto generateReport(String address, String categoryCode) {
        List<KakaoPlaceDto> places = kakaoMapClient.searchNearbyBusinesses(address, categoryCode);

        List<CompanyDto> competitorsWithIndividualSummary = new ArrayList<>();
        for (KakaoPlaceDto place : places) {
            String description = geminiApiClient.generateCompanyDescription(place);
            competitorsWithIndividualSummary.add(
                    new CompanyDto(place.getName(), place.getAddress(), description)
            );

            try {
                // 각 호출 사이에 1초(1000밀리초)의 딜레이를 줍니다.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        String overallSummary = geminiApiClient.summarizeOverallCommercialArea(competitorsWithIndividualSummary);

        saveReport(address, categoryCode, overallSummary);

        return new AiReportResponseDto(competitorsWithIndividualSummary, overallSummary);
    }

    private void saveReport(String address, String categoryCode, String overallSummary) {
        Report newReport = Report.builder()
                .address(address)
                .category(categoryCode)
                .summary(overallSummary)
                .build();
        reportRepository.save(newReport);
    }
}
