package com.vintan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.component.GeminiApiClient;
import com.vintan.component.KakaoMapClient;
import com.vintan.domain.Competitor;
import com.vintan.domain.Report;
import com.vintan.domain.User;
import com.vintan.dto.aiReport.CompanyDto;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import com.vintan.embedded.FinalScore;
import com.vintan.repository.ReportRepository;
import com.vintan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final KakaoMapClient kakaoMapClient;
    private final GeminiApiClient geminiApiClient;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Transactional
    public Report generateCompetitionReport(String address, String categoryCode, String userId) {

        User writer = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("cannot find user"));

        List<KakaoPlaceDto> places = kakaoMapClient.searchNearbyBusinesses(address, categoryCode);


        List<CompanyDto> competitorsWithDesc = new ArrayList<>();
        for (KakaoPlaceDto place : places) {
            String description = geminiApiClient.generateCompanyDescription(place);
            competitorsWithDesc.add(
                    new CompanyDto(place.getName(), place.getAddress(), description)
            );
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
        }

        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        String competitionAnalysisJson = geminiApiClient.analyzeCompetition(competitorsWithDesc);

        try {
            JsonNode analysisNode = objectMapper.readTree(competitionAnalysisJson);
            String competitionSummary = analysisNode.path("summary").asText();
            int competitionScore = analysisNode.path("score").asInt();


            FinalScore finalScore = FinalScore.builder()
                    .competitionScore(competitionScore)
                    // 다른 점수들은 나중에 채워질 예정이므로 0으로 초기화
                    .build();


            Report newReport = Report.builder()
                    .address(address)
                    .category(categoryCode)
                    .finalScore(finalScore)
                    .competitorSummary(competitionSummary)
                    .user(writer)
                    .build();


            for (CompanyDto compDto : competitorsWithDesc) {
                Competitor competitor = Competitor.builder()
                        .name(compDto.getName())
                        .address(compDto.getAddress())
                        .description(compDto.getDescription())
                        .build();
                newReport.addCompetitor(competitor);
            }


            return reportRepository.save(newReport);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gemini 경쟁 분석 응답 파싱에 실패했습니다.");
        }
    }
}