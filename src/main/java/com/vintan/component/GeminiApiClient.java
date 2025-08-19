package com.vintan.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.dto.aiReport.CompanyDto;
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

@Component
@RequiredArgsConstructor
public class GeminiApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BlindCommunityPostService blindCommunityPostService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateCompanyDescription(KakaoPlaceDto place) {
        String prompt = String.format(
                "'%s'는(은) '%s' 주소에 위치한 '%s' 카테고리의 가게입니다. 이 가게의 특징과 컨셉을 한 문장으로 설명해주세요.",
                place.getName(), place.getAddress(), place.getCategory()
        );
        return callGeminiApi(prompt, "개별 업체 설명 생성에 실패했습니다.");
    }

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

    public GeneralOverviewGeminiDto generateOverallReview(Long regionId) {
        CommunityAllReviewResponseDto responseDto = blindCommunityPostService.getAllPost(regionId);

        try {
            // 1. responseDto 객체를 JSON 문자열로 변환합니다.
            String communityDataAsJson = objectMapper.writeValueAsString(responseDto);

            // 2. 변환된 JSON 문자열을 프롬프트에 넣습니다.
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
                            "  \"summary\": \"최종 요약 내용\",\n" +
                            "  \"positive\": \"커뮤니티 전반적인 긍정적인 분석 내용\",\n" +
                            "  \"negative\": \"커뮤니티 부정적인 긍정적인 분석 내용\",\n" +
                            "  \"score\": 25점_만점의_점수\n" +
                            "}\n" +
                            "```",
                    communityDataAsJson // 수정된 부분
            );
            String geminiOutput = callGeminiApi(prompt, "접근성 분석 생성에 실패했습니다.");
            return new GeneralOverviewGeminiDto(geminiOutput, responseDto);

        } catch (JsonProcessingException e) {
            // JSON 변환 중 에러가 발생했을 때의 처리
            // 예를 들어, 로깅을 하거나 사용자 정의 예외를 던질 수 있습니다.
            throw new RuntimeException("커뮤니티 데이터 JSON 변환에 실패했습니다.", e);
        }
    }

    private String callGeminiApi(String prompt, String errorMessage) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
            String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
            JsonNode root = objectMapper.readTree(response);

            String resultText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

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