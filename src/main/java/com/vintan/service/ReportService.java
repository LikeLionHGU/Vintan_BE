package com.vintan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.component.GeminiApiClient;
import com.vintan.component.KakaoMapClient;
import com.vintan.component.PohangBusApiClient;
import com.vintan.domain.Competitor;
import com.vintan.domain.Report;
import com.vintan.domain.User;
import com.vintan.dto.aiReport.CompanyDto;
import com.vintan.dto.response.ai.KakaoAddressResponse;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import com.vintan.embedded.AccessibilityAnalysis;
import com.vintan.embedded.FinalScore;
import com.vintan.embedded.FloatingPopulationAnalysis;
import com.vintan.repository.ReportRepository;
import com.vintan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final KakaoMapClient kakaoMapClient;
    private final GeminiApiClient geminiApiClient;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PohangBusApiClient pohangBusApiClient;
    private final ObjectMapper objectMapper;

    @Transactional
    public Report generateFullReport(String address, String categoryCode, String userId) {
        // --- 1. 사전 준비: 사용자 조회 및 주소->좌표 변환 ---
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다: " + userId));

        KakaoAddressResponse.Document coordinate = kakaoMapClient.getCoordinatesFromAddress(address);
        if (coordinate == null) {
            throw new RuntimeException("주소의 좌표를 찾을 수 없습니다.");
        }
        double latitude = Double.parseDouble(coordinate.latitude());
        double longitude = Double.parseDouble(coordinate.longitude());

        // --- 2. 데이터 수집: 각 API 클라이언트 호출 ---
        List<KakaoPlaceDto> places = kakaoMapClient.searchNearbyBusinesses(address, categoryCode);
        List<String> landmarks = kakaoMapClient.findPlaceNamesByCategory(longitude, latitude, "AT4");
        List<String> stations = kakaoMapClient.findPlaceNamesByCategory(longitude, latitude, "SW8");
        List<String> busRoutes = pohangBusApiClient.getBusRoutesNear(latitude, longitude);

        // --- 3. Gemini 분석 (두 가지를 한 번에 또는 순차적으로 진행) ---
        // 여기서는 개별 요약 -> 경쟁 분석 -> 접근성 분석 순으로 진행

        // 3-1. 개별 경쟁업체 설명 생성
        List<CompanyDto> competitorsWithDesc = new ArrayList<>();
        for (KakaoPlaceDto place : places) {
            String description = geminiApiClient.generateCompanyDescription(place);
            competitorsWithDesc.add(new CompanyDto(place.getName(), place.getAddress(), description));
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        // 3-2. 경쟁 강도 분석
        String competitionAnalysisJson = geminiApiClient.analyzeCompetition(competitorsWithDesc);

        // 3-3. 접근성 분석
        String accessibilityAnalysisJson = geminiApiClient.generateAccessibilityAnalysis(address, landmarks, stations, busRoutes);

        String floatingPopulationAnalysisJson = geminiApiClient.generatefloatingPopulationAnalysisJson(address);

        try {
            // --- 4. Gemini 응답 파싱 및 엔티티 조립 ---
            JsonNode competitionNode = objectMapper.readTree(competitionAnalysisJson);
            JsonNode accessibilityNode = objectMapper.readTree(accessibilityAnalysisJson);
            JsonNode floatingPopulationNode = objectMapper.readTree(floatingPopulationAnalysisJson);

            // 각 분석 결과 객체 생성
            AccessibilityAnalysis accessibilityAnalysis = objectMapper.treeToValue(accessibilityNode, AccessibilityAnalysis.class);
            FloatingPopulationAnalysis floatingPopulationAnalysis = objectMapper.treeToValue(floatingPopulationNode, FloatingPopulationAnalysis.class);

            // FinalScore 객체 생성
            int competitionScore = competitionNode.path("score").asInt();
            int accessibilityScore = accessibilityAnalysis.getScore();
            int floatingPopulationScore = floatingPopulationNode.path("score").asInt();
            int totalScore = competitionScore + accessibilityScore + floatingPopulationScore;

            FinalScore finalScore = FinalScore.builder()
                    .competitionScore(competitionScore)
                    .accessibilityScore(accessibilityScore)
                    .floatingPopulationScore(floatingPopulationScore)
                    .totalScore(totalScore)
                    .build();

            // Report 엔티티 최종 생성
            Report newReport = Report.builder()
                    .address(address)
                    .category(categoryCode)
                    .user(writer)
                    .competitorSummary(competitionNode.path("summary").asText())
                    .accessibilityAnalysis(accessibilityAnalysis)
                    .floatingPopulationAnalysis(floatingPopulationAnalysis)
                    .finalScore(finalScore)
                    .build();

            // Competitor 엔티티들 생성 및 Report에 추가
            for (CompanyDto compDto : competitorsWithDesc) {
                Competitor competitor = Competitor.builder()
                        .name(compDto.getName())
                        .address(compDto.getAddress())
                        .description(compDto.getDescription())
                        .build();
                newReport.addCompetitor(competitor);
            }

            // --- 5. DB에 최종 저장 ---
            return reportRepository.save(newReport);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gemini 응답 파싱 또는 리포트 생성에 실패했습니다.");
        }
    }
}