package com.vintan.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintan.dto.aiReport.CompanyDto;
import com.vintan.dto.response.ai.KakaoPlaceDto;
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

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateCompanyDescription(KakaoPlaceDto place) {
        String prompt = String.format(
                "'%s'는(은) '%s' 주소에 위치한 '%s' 카테고리의 가게입니다. 이 가게의 특징과 컨셉을 한 문장으로 설명해주세요.",
                place.getName(), place.getAddress(), place.getCategory()
        );
        return callGeminiApi(prompt, "개별 업체 설명 생성에 실패했습니다.");
    }

    public String summarizeOverallCommercialArea(List<CompanyDto> competitors) {
        StringBuilder promptBuilder = new StringBuilder("다음은 한 지역의 경쟁 업체들 각각에 대한 요약 정보입니다:\n");
        for (CompanyDto competitor : competitors) {
            promptBuilder.append(String.format("- %s: %s\n", competitor.getName(), competitor.getDescription()));
        }
        promptBuilder.append("\n이 정보들을 종합해서, 이 상권의 전체적인 특징, 강점, 약점을 한국어로 3~4문장으로 요약해주세요.");

        return callGeminiApi(promptBuilder.toString(), "최종 상권 요약 생성에 실패했습니다.");
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
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return errorMessage;
        }
    }
}