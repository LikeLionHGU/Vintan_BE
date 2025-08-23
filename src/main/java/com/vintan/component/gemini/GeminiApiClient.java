package com.vintan.component.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.dto.request.ai.kakao.CompanyDto;
import com.vintan.dto.response.ai.GeneralOverviewGeminiDto;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import com.vintan.service.BlindCommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * GeminiApiClient is responsible for interacting with Google's Gemini API
 * to generate various business-related analysis and reports using AI.
 */
@Component
@RequiredArgsConstructor
public class GeminiApiClient {

    private final RestTemplate restTemplate; // Used to make HTTP requests
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization/deserialization
    private final BlindCommunityPostService blindCommunityPostService; // Service for fetching community reviews

    @Value("${gemini.api.key}")
    private String geminiApiKey; // API key for Gemini API, injected from application properties

    /**
     * Generates a one-sentence description of a company based on its name, address, and category.
     */
    public String generateCompanyDescription(KakaoPlaceDto place) {
        String prompt = String.format(
                "'%s'는(은) '%s' 주소에 위치한 '%s' 카테고리의 가게입니다. 이 가게의 특징과 컨셉을 한 문장으로 설명해주세요.",
                place.getName(), place.getAddress(), place.getCategory()
        );
        return callGeminiApi(prompt, "개별 업체 설명 생성에 실패했습니다.");
    }

    /**
     * Analyzes the competition in a given area using a list of competitor companies.
     * Returns a JSON string containing summary and score (25 points max).
     */
    public String analyzeCompetition(List<CompanyDto> competitors) {
        StringBuilder promptBuilder = new StringBuilder("다음은 한 지역의 경쟁 업체들 각각에 대한 요약 정보입니다:\n");
        for (CompanyDto competitor : competitors) {
            promptBuilder.append(String.format("- %s: %s\n", competitor.getName(), competitor.getDescription()));
        }
        promptBuilder.append("\n이 정보들을 종합해서, 이 상권의 '경쟁 강도'에 대한 분석과 25점 만점의 점수를 포함하여 응답해줘.");
        promptBuilder.append("응답은 반드시 아래의 마크다운 코드 블록 형식에 맞춰서 다른 말 없이 JSON 데이터만 반환해줘:\n");
        promptBuilder.append("```json\n");
        promptBuilder.append("{\n");
        promptBuilder.append("  \"summary\": \"경쟁 강도에 대한 종합 분석 내용\",\n");
        promptBuilder.append("  \"score\": 25점_만점의_점수\n");
        promptBuilder.append("}\n");
        promptBuilder.append("```");

        return callGeminiApi(promptBuilder.toString(), "경쟁 강도 분석에 실패했습니다.");
    }

    /**
     * Generates an accessibility and surrounding facilities analysis report.
     * Includes parking, landmarks, public transport, station info, summary, and a score.
     */
    public String generateAccessibilityAnalysis(String address, List<String> landmarks, List<String> stations, List<String> busRoutes) {
        String prompt = String.format(
                "너는 상권 분석 전문가야. 아래 데이터를 바탕으로 '접근성 및 주변 시설 분석' 보고서를 작성해줘.\n\n" +
                        "[입력 데이터]\n" +
                        "- 분석 주소: %s\n" +
                        "- 주변 주요 랜드마크 (반경 1km): %s\n" +
                        "- 주변 지하철/기차역 (반경 1km): %s\n" +
                        "- 주변 버스 노선: %s\n\n" +
                        "[요청 사항]\n" +
                        "1. landmark: 수집된 랜드마크 정보를 바탕으로 이 지역의 상권 특징을 분석해줘. (데이터가 없으면 그냥 위치를 기반으로 해서 정보 제공해줘)\n" +
                        "2. parking: 이 지역에 해당하는 주차 환경은 어떤 것 같은지 분석해줘\n" +
                        "3. publicTransport: 수집된 버스 노선 정보를 바탕으로 대중교통 편의성을 분석해줘.(데이터가 없으면 그냥 위치를 기반으로 해서 정보 제공해줘)\n" +
                        "4. stationInfo: 수집된 역 정보를 바탕으로 광역 교통 편의성을 분석해줘.(데이터가 없으면 그냥 위치를 기반으로 해서 정보 제공해줘)\n" +
                        "5. summary: 위 모든 정보를 종합한 최종 요약을 작성해줘.\n" +
                        "6. score: 위 모든 것을 고려한 최종 접근성 점수를 25점 만점으로 알려줘.\n\n" +
                        "응답은 반드시 다음 JSON 형식에 맞춰서 다른 말 없이 데이터만 반환해줘:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"summary\": \"최종 요약 내용\",\n" +
                        "  \"parking\": \"주차비 분석 내용\",\n" +
                        "  \"landmark\": \"랜드마크 분석 내용\",\n" +
                        "  \"publicTransport\": \"대중교통 분석 내용\",\n" +
                        "  \"stationInfo\": \"역/광역 분석 내용\",\n" +
                        "  \"score\": 25점_만점의_점수\n" +
                        "}\n" +
                        "```",
                address,
                String.join(", ", landmarks),
                String.join(", ", stations),
                String.join(", ", busRoutes)
        );

        return callGeminiApi(prompt, "접근성 분석 생성에 실패했습니다.");
    }

    /**
     * Generates an analysis report for floating population around a given address.
     */
    public String generatefloatingPopulationAnalysisJson(String address) {
        String prompt = String.format(
                "너는 상권 분석 전문가야. 아래 데이터를 바탕으로 '유동인구' 보고서를 작성해줘.\n\n" +
                        "[입력 데이터]\n" +
                        "- 분석 주소: %s\n" +
                        "[요청 사항]\n" +
                        "1. weekdayAnalysis: 이 주변지역에 평일에는 유동인구가 어떠한지 분석해줘\n" +
                        "2. weekendAnalysis: 이 주변지역에 주말에는 유동인구가 어떠한지 분석해줘\n" +
                        "3. nearbyFacilities: 이 주변지역에 주별시설 영향이 유동인구에 어떠한지 분석해줘\n" +
                        "4. ageGroup: 이 주변지역에 연령대의 유동인구는 어떻게 형성되어있는지 분석해줘\n" +
                        "5. summary: 위 모든 정보를 종합한 최종 요약을 작성해줘.\n" +
                        "6. score: 위 모든 것을 고려한 최종 유동인구 점수를 25점 만점으로 알려줘.\n\n" +
                        "응답은 반드시 다음 JSON 형식에 맞춰서 다른 말 없이 데이터만 반환해줘:\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"summary\": \"최종 요약 내용\",\n" +
                        "  \"weekdayAnalysis\": \"평이 분석 내용\",\n" +
                        "  \"weekendAnalysis\": \"주말 분석 내용\",\n" +
                        "  \"nearbyFacilities\": \"주변시설 분석 내용\",\n" +
                        "  \"ageGroup\": \"연령대별 분석 내용\",\n" +
                        "  \"score\": 25점_만점의_점수\n" +
                        "}\n" +
                        "```",
                address
        );

        return callGeminiApi(prompt, "접근성 분석 생성에 실패했습니다.");
    }

    /**
     * Generates an overview review analysis for a specific region by fetching community posts and analyzing them.
     */
    public GeneralOverviewGeminiDto generateOverallReview(Long regionId) {
        CommunityAllReviewResponseDto responseDto = blindCommunityPostService.getAllPost(regionId);
        int postCount = responseDto.getBlind().size();

        try {
            // Convert responseDto to JSON string for inclusion in prompt
            String communityDataAsJson = objectMapper.writeValueAsString(responseDto);

            // Build the prompt for Gemini
            String prompt = String.format(
                    "너는 상권 분석 전문가야. 아래 데이터를 바탕으로 '전체 적인 커뮤니티' 보고서를 작성해줘.\n\n" +
                            "[입력 데이터]\n" +
                            "- 커뮤니티 정보: %s\n" +
                            "[요청 사항]\n" +
                            "1. positive: 커뮤니티가 분석한 장점 (정보가 없으면 \"커뮤니티 글 없음\" 이렇게 나오게 해줘)\n" +
                            "2. negative: (정보가 없으면 \"커뮤니티 글 없음\" 이렇게 나오게 해줘)\n" +
                            "3. summary: 위 모든 정보를 종합한 최종 요약을 작성해줘.(정보가 없으면 \"커뮤니티 글 없음\" 이렇게 나오게 해줘)\n" +
                            "4. score: 위 모든 것을 고려한 최종 접근성 점수를 25점 만점으로 알려줘.\n\n" +
                            "응답은 반드시 다음 JSON 형식에 맞춰서 다른 말 없이 데이터만 반환해줘:\n" +
                            "```json\n" +
                            "{\n" +
                            "  \"review_summary\": \"최종 요약 내용\",\n" +
                            "  \"positive\": \"커뮤니티 전반적인 긍정적인 분석 내용\",\n" +
                            "  \"negative\": \"커뮤니티 부정적인 긍정적인 분석 내용\",\n" +
                            "  \"review_score\": 25점_만점의_점수\n" +
                            "}\n" +
                            "```",
                    communityDataAsJson
            );
            String geminiOutput = callGeminiApi(prompt, "접근성 분석 생성에 실패했습니다.");
            return new GeneralOverviewGeminiDto(geminiOutput, responseDto, postCount);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("커뮤니티 데이터 JSON 변환에 실패했습니다.", e);
        }
    }

    /**
     * Generates a final comprehensive business report combining all analyses and scores.
     */
    public String generateFinalReport(String competitionAnalysisJson,
                                      String accessibilityAnalysisJson,
                                      String floatingPopulationAnalysisJson,
                                      GeneralOverviewGeminiDto genearlOverviewGeminiDto,
                                      String address,
                                      String categoryCode,
                                      double pyeong,
                                      String userDetailNeeds,
                                      int competitionScore,
                                      int accessibilityScore,
                                      int floatingPopulationScore,
                                      int generalOverviewScore,
                                      int totalScore) {
        String prompt = String.format(
                "너는 대한민국 최고의 상권 분석가이자 창업 컨설턴트야. 아래의 모든 데이터를 종합해서 예비 창업자를 위한 최종 보고서를 생성해줘.\n\n" +
                        "[기본 정보]\n" +
                        "- 창업 주소: %s\n" +
                        "- 업종: %s\n" +
                        "- 매장 평수: %.1f평\n" +
                        "- 사용자 요청사항: %s\n\n" +
                        "[분석 데이터 요약]\n" +
                        "- 경쟁 환경 분석 결과: %s\n" +
                        "- 접근성 및 주변 시설 분석 결과: %s\n" +
                        "- 유동인구 분석 결과: %s\n" +
                        "- 커뮤니티 리뷰 분석 결과: %s\n\n" +
                        "[종합 점수]\n" +
                        "- 경쟁 점수: %d/25점\n" +
                        "- 접근성 점수: %d/25점\n" +
                        "- 유동인구 점수: %d/25점\n" +
                        "- 커뮤니티 점수: %d/25점\n" +
                        "- 최종 종합 점수: %d/100점\n\n" +
                        "[요청 사항]\n" +
                        "1. 위 모든 정보를 바탕으로, '최종 업종 적합도는 %d점 입니다.' 라는 문장으로 시작하는 최종 리포트를 작성해줘.\n" +
                        "2. '다음과 같은 실행을 제안합니다.' 라는 문장 아래에, 전문적이면서도 이해하기 쉬운 구체적인 실행 방안 6가지를 제안해줘.\n" +
                        "3. 각 제안은 다음 항목을 반드시 포함해야 하며, 주어진 데이터를 적극적으로 활용해서 현실적인 조언을 해줘야 해.\n" +
                        "   - 1. 좌석 구성: 매장 평수와 업종을 고려한 테이블 배치, 동선 확보 방안 제안\n" +
                        "   - 2. 메뉴 전략: 경쟁 환경과 주변 상권 특징을 고려한 런치/디너 메뉴 구성 및 가격대 제안\n" +
                        "   - 3. 주차/정산: 접근성 분석 결과를 바탕으로 한 주차 문제 해결 방안 및 정산 전략 제안\n" +
                        "   - 4. 가시성: 주변 환경을 고려한 간판, 조명(lx 단위 언급 포함) 등 시각적 어필 전략 제안\n" +
                        "   - 5. 예약/대기: 유동인구 특성과 업종을 고려한 효율적인 예약 및 대기 시스템 제안\n" +
                        "   - 6. 제휴: 주변 시설(아파트, 오피스, 관공서 등)을 활용한 타겟 고객 제휴 마케팅 방안 제안\n" +
                        "4. 응답은 다른 설명 없이 최종 보고서 내용만 깔끔하게 출력해줘.",
                address,
                categoryCode,
                pyeong,
                userDetailNeeds,
                competitionAnalysisJson,
                accessibilityAnalysisJson,
                floatingPopulationAnalysisJson,
                genearlOverviewGeminiDto.getOutput(),
                competitionScore,
                accessibilityScore,
                floatingPopulationScore,
                generalOverviewScore,
                totalScore,
                totalScore
        );

        return callGeminiApi(prompt, "최종 보고서 생성에 실패했습니다.");
    }

    /**
     * Calls Gemini API with the given prompt and returns the generated text response.
     * Handles response parsing and JSON extraction.
     */
    private String callGeminiApi(String prompt, String errorMessage) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare request body for Gemini API
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            // Send POST request to Gemini API
            String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);

            // Parse JSON response
            JsonNode root = objectMapper.readTree(response);

            // Extract AI-generated text
            String resultText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            // If response starts with ```json, strip markdown code block
            if (resultText.startsWith("```json")) {
                resultText = resultText.substring(7, resultText.length() - 3).trim();
            }
            return resultText;

        } catch (Exception e) {
            e.printStackTrace();
            return errorMessage;
        }
    }
}
