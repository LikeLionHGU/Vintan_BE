package com.vintan.component;

import com.vintan.dto.aiReport.CompanyDto;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode; // Jackson import
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson import

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public List<KakaoPlaceDto> searchNearbyBusinesses(String address, String categoryCode) {
        try {
            String urlForCoords = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;
            HttpEntity<String> entityForCoords = createHttpEntity();
            ResponseEntity<String> coordsResponse = restTemplate.exchange(urlForCoords, HttpMethod.GET, entityForCoords, String.class);

            JsonNode coordsRoot = objectMapper.readTree(coordsResponse.getBody());
            if (coordsRoot.path("documents").isEmpty()) {
                return Collections.emptyList();
            }
            String longitude = coordsRoot.path("documents").get(0).path("x").asText();
            String latitude = coordsRoot.path("documents").get(0).path("y").asText();

            String urlForPlaces = String.format("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=%s&x=%s&y=%s&radius=2000",
                    categoryCode, longitude, latitude);
            HttpEntity<String> entityForPlaces = createHttpEntity();
            ResponseEntity<String> placesResponse = restTemplate.exchange(urlForPlaces, HttpMethod.GET, entityForPlaces, String.class);

            JsonNode placesRoot = objectMapper.readTree(placesResponse.getBody());

            List<KakaoPlaceDto> companies = new ArrayList<>();
            for (JsonNode doc : placesRoot.path("documents")) {
                companies.add(new KakaoPlaceDto(
                        doc.path("place_name").asText(),
                        doc.path("road_address_name").asText(),
                        doc.path("category_name").asText()
                ));
                if (companies.size() >= 10) break;
            }
            return companies;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        return new HttpEntity<>(headers);
    }
}