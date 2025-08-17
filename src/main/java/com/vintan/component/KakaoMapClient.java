package com.vintan.component;


import com.vintan.dto.response.ai.KakaoAddressResponse;
import com.vintan.dto.response.ai.KakaoCategoryResponse;
import com.vintan.dto.response.ai.KakaoPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public List<KakaoPlaceDto> searchNearbyBusinesses(String address, String categoryCode) {
        KakaoAddressResponse.Document coordinate = getCoordinatesFromAddress(address);
        if (coordinate == null) {
            return Collections.emptyList();
        }

        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json")
                .queryParam("category_group_code", categoryCode)
                .queryParam("x", coordinate.longitude())
                .queryParam("y", coordinate.latitude())
                .queryParam("radius", 500)
                .queryParam("size", 5)
                .encode()
                .build()
                .toUri();

        HttpEntity<String> entity = createHttpEntity();
        KakaoCategoryResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoCategoryResponse.class).getBody();

        if (response == null || response.documents() == null) {
            return Collections.emptyList();
        }

        return response.documents().stream()
                .map(doc -> new KakaoPlaceDto(doc.placeName(), doc.roadAddressName(), doc.categoryName()))
                .collect(Collectors.toList());
    }

    public List<String> findPlaceNamesByCategory(Double longitude, Double latitude, String categoryCode) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json")
                .queryParam("category_group_code", categoryCode)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", 1000)
                .encode()
                .build()
                .toUri();

        HttpEntity<String> entity = createHttpEntity();
        KakaoCategoryResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoCategoryResponse.class).getBody();

        if (response == null || response.documents() == null) {
            return Collections.emptyList();
        }


        return response.documents().stream()
                .map(KakaoCategoryResponse.PlaceDocument::placeName)
                .collect(Collectors.toList());
    }

    public KakaoAddressResponse.Document getCoordinatesFromAddress(String address) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .encode()
                .build()
                .toUri();

        HttpEntity<String> entity = createHttpEntity();
        KakaoAddressResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoAddressResponse.class).getBody();

        if (response != null && !response.documents().isEmpty()) {
            return response.documents().getFirst();
        }
        return null;
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        return new HttpEntity<>(headers);
    }
}