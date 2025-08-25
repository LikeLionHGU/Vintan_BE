package com.vintan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.component.gemini.GeminiApiClient;
import com.vintan.component.kakaomap.KakaoMapClient;
import com.vintan.component.pohangbus.PohangBusApiClient;
import com.vintan.controller.converter.CategoryConverter;
import com.vintan.domain.*;
import com.vintan.dto.request.ai.kakao.CompanyDto;
import com.vintan.dto.request.ai.ReportRequestDto;
import com.vintan.dto.response.ai.GeneralOverviewGeminiDto;
import com.vintan.dto.response.ai.record.KakaoAddressResponse;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import com.vintan.domain.embedded.AccessibilityAnalysis;
import com.vintan.domain.embedded.FinalScore;
import com.vintan.domain.embedded.FloatingPopulationAnalysis;
import com.vintan.domain.embedded.OverallReview;
import com.vintan.repository.BlindCommunityPostRepository;
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
    private final BlindCommunityPostRepository blindCommunityPostRepository;

    /** Get existing report by ID */
    public Report getReport(Long reportId) {
        return reportRepository.getReportById(reportId);
    }

    /**
     * Generate a full report including competitor, accessibility, floating population, and overall analysis
     */
    @Transactional
    public Report generateFullReport(ReportRequestDto requestDto, String userId, Long regionId) {
        String address = requestDto.getAddress();
        String categoryCode = CategoryConverter.getCategoryCode(requestDto.getCategoryCode());
        double pyeong = requestDto.getPyeong();
        String userDetail = requestDto.getUserDetail();

        // --- 1. Preprocessing: fetch user & convert address to coordinates ---
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        KakaoAddressResponse.Document coordinate = kakaoMapClient.getCoordinatesFromAddress(address);
        if (coordinate == null) {
            throw new RuntimeException("Coordinates not found for the given address.");
        }
        double latitude = Double.parseDouble(coordinate.latitude());
        double longitude = Double.parseDouble(coordinate.longitude());

        // --- 2. Data collection from external APIs ---
        List<KakaoPlaceDto> nearbyPlaces = kakaoMapClient.searchNearbyBusinesses(address, categoryCode);
        List<String> landmarks = kakaoMapClient.findPlaceNamesByCategory(longitude, latitude, "AT4");
        List<String> stations = kakaoMapClient.findPlaceNamesByCategory(longitude, latitude, "SW8");
        List<String> busRoutes = pohangBusApiClient.getBusRoutesNear(latitude, longitude);
        List<BlindCommunityPost> posts = blindCommunityPostRepository.findByRegionNo(regionId);

        // --- 3. Gemini analysis ---
        // 3-1. Generate individual competitor descriptions
        List<CompanyDto> competitorsWithDesc = new ArrayList<>();
        for (KakaoPlaceDto place : nearbyPlaces) {
            String description = geminiApiClient.generateCompanyDescription(place);
            competitorsWithDesc.add(new CompanyDto(place.getName(), place.getAddress(), description));
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        // 3-2. Competition analysis
        String competitionAnalysisJson = geminiApiClient.analyzeCompetition(competitorsWithDesc);

        // 3-3. Accessibility analysis
        String accessibilityAnalysisJson = geminiApiClient.generateAccessibilityAnalysis(address, landmarks, stations, busRoutes);

        // 3-4. Floating population analysis
        String floatingPopulationAnalysisJson = geminiApiClient.generatefloatingPopulationAnalysisJson(address);

        // 3-5. Overall review analysis
        GeneralOverviewGeminiDto generalOverviewGeminiDto = geminiApiClient.generateOverallReview(posts);
        String generalOverviewJson = generalOverviewGeminiDto.getOutput();
        CommunityAllReviewResponseDto communityAllReviewResponseDto = generalOverviewGeminiDto.getCommunityAllReviewResponseDto();
        int postReportCount = generalOverviewGeminiDto.getPostCount();

        try {
            // --- 4. Parse JSON responses and assemble entities ---
            JsonNode competitionNode = objectMapper.readTree(competitionAnalysisJson);
            JsonNode accessibilityNode = objectMapper.readTree(accessibilityAnalysisJson);
            JsonNode floatingPopulationNode = objectMapper.readTree(floatingPopulationAnalysisJson);
            JsonNode generalOverviewNode = objectMapper.readTree(generalOverviewJson);

            AccessibilityAnalysis accessibilityAnalysis = objectMapper.treeToValue(accessibilityNode, AccessibilityAnalysis.class);
            FloatingPopulationAnalysis floatingPopulationAnalysis = objectMapper.treeToValue(floatingPopulationNode, FloatingPopulationAnalysis.class);
            OverallReview overallReview = objectMapper.treeToValue(generalOverviewNode, OverallReview.class);

            // Calculate scores
            int competitionScore = competitionNode.path("score").asInt();
            int accessibilityScore = accessibilityAnalysis.getScore();
            int floatingPopulationScore = floatingPopulationNode.path("score").asInt();
            int generalOverviewScore = generalOverviewNode.path("review_score").asInt();
            int totalScore = competitionScore + accessibilityScore + floatingPopulationScore + generalOverviewScore;

            // Generate final report text
            String finalReportAnalysisOutput = geminiApiClient.generateFinalReport(
                    competitionAnalysisJson,
                    accessibilityAnalysisJson,
                    floatingPopulationAnalysisJson,
                    generalOverviewGeminiDto,
                    address,
                    categoryCode,
                    pyeong,
                    userDetail,
                    competitionScore,
                    accessibilityScore,
                    floatingPopulationScore,
                    generalOverviewScore,
                    totalScore
            );

            // Create FinalScore entity
            FinalScore finalScore = FinalScore.builder()
                    .competitionScore(competitionScore)
                    .accessibilityScore(accessibilityScore)
                    .floatingPopulationScore(floatingPopulationScore)
                    .overallReviewScore(generalOverviewScore)
                    .totalScore(totalScore)
                    .build();

            // Assemble Report entity
            Report newReport = Report.builder()
                    .address(address)
                    .category(categoryCode)
                    .user(writer)
                    .competitorSummary(competitionNode.path("summary").asText())
                    .accessibilityAnalysis(accessibilityAnalysis)
                    .floatingPopulationAnalysis(floatingPopulationAnalysis)
                    .overallReview(overallReview)
                    .averageCleannessScore(communityAllReviewResponseDto.getClean())
                    .averageCommunityScore(communityAllReviewResponseDto.getTotalRate())
                    .averagePopulationScore(communityAllReviewResponseDto.getPeople())
                    .averageReachabilityScore(communityAllReviewResponseDto.getAccessibility())
                    .averageRentFeeScore(communityAllReviewResponseDto.getRentFee())
                    .finalReportSummary(finalReportAnalysisOutput)
                    .finalScore(finalScore)
                    .addressName(requestDto.getAddressName())
                    .postCount(postReportCount)
                    .build();

            // Create Competitor entities and attach to Report
            for (CompanyDto compDto : competitorsWithDesc) {
                Competitor competitor = Competitor.builder()
                        .name(compDto.getName())
                        .address(compDto.getAddress())
                        .description(compDto.getDescription())
                        .build();
                newReport.addCompetitor(competitor);
            }

            // --- 5. Save the final report in DB ---
            return reportRepository.save(newReport);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse Gemini response or generate report.");
        }
    }
}
