package com.vintan.component.kakaomap;

import com.vintan.dto.response.ai.record.KakaoAddressResponse;
import com.vintan.dto.response.ai.record.KakaoCategoryResponse;
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
import java.util.stream.Collectors;

/**
 * KakaoMapClient is responsible for communicating with Kakao Map API
 * to retrieve geographical and place-related data such as coordinates,
 * nearby businesses, and places by category.
 */
@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    private final RestTemplate restTemplate; // Used for making HTTP requests to Kakao API

    @Value("${kakao.api.key}")
    private String kakaoApiKey; // Kakao REST API key, injected from application properties

    /**
     * Searches for nearby businesses based on the given address and category.
     *
     * @param address      The address to search around.
     * @param categoryCode Kakao category group code (e.g., FD6 for restaurants).
     * @return A list of KakaoPlaceDto objects containing place details.
     */
    public List<KakaoPlaceDto> searchNearbyBusinesses(String address, String categoryCode) {
        // Step 1: Convert address to coordinates
        KakaoAddressResponse.Document coordinate = getCoordinatesFromAddress(address);
        if (coordinate == null) {
            // If coordinates cannot be retrieved, return an empty list
            return Collections.emptyList();
        }

        // Step 2: Build the API URI for category search (500m radius, max 5 results)
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json")
                .queryParam("category_group_code", categoryCode) // Category group code
                .queryParam("x", coordinate.longitude())          // Longitude from address
                .queryParam("y", coordinate.latitude())           // Latitude from address
                .queryParam("radius", 500)                        // Search radius in meters
                .queryParam("size", 5)                            // Limit to 5 results
                .encode()
                .build()
                .toUri();

        // Step 3: Create HTTP headers with Kakao API key
        HttpEntity<String> entity = createHttpEntity();

        // Step 4: Make the GET request to Kakao API
        KakaoCategoryResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoCategoryResponse.class).getBody();

        // Step 5: If no response or no documents found, return empty list
        if (response == null || response.documents() == null) {
            return Collections.emptyList();
        }

        // Step 6: Map Kakao API documents to KakaoPlaceDto and return
        return response.documents().stream()
                .map(doc -> new KakaoPlaceDto(doc.placeName(), doc.roadAddressName(), doc.categoryName()))
                .collect(Collectors.toList());
    }

    /**
     * Finds place names near the given coordinates for a specific category.
     *
     * @param longitude    Longitude coordinate.
     * @param latitude     Latitude coordinate.
     * @param categoryCode Kakao category group code.
     * @return A list of place names.
     */
    public List<String> findPlaceNamesByCategory(Double longitude, Double latitude, String categoryCode) {
        // Build the API URI for category-based search (1km radius)
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json")
                .queryParam("category_group_code", categoryCode)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", 1000) // Larger radius than previous method
                .encode()
                .build()
                .toUri();

        // Create headers with Kakao API key
        HttpEntity<String> entity = createHttpEntity();

        // Call Kakao API
        KakaoCategoryResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoCategoryResponse.class).getBody();

        if (response == null || response.documents() == null) {
            return Collections.emptyList();
        }

        // Extract and return only place names
        return response.documents().stream()
                .map(KakaoCategoryResponse.PlaceDocument::placeName)
                .collect(Collectors.toList());
    }

    /**
     * Converts a given address into geographical coordinates using Kakao Address API.
     *
     * @param address The address to convert.
     * @return A KakaoAddressResponse.Document containing latitude and longitude, or null if not found.
     */
    public KakaoAddressResponse.Document getCoordinatesFromAddress(String address) {
        // Build the URI for address search
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .encode()
                .build()
                .toUri();

        // Prepare headers
        HttpEntity<String> entity = createHttpEntity();

        // Call Kakao API for address search
        KakaoAddressResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoAddressResponse.class).getBody();

        // If response contains documents, return the first one
        if (response != null && !response.documents().isEmpty()) {
            return response.documents().getFirst();
        }
        return null;
    }

    /**
     * Creates an HttpEntity with the required headers for Kakao API requests.
     * This includes the Authorization header with Kakao API key.
     */
    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey); // Kakao API authentication
        return new HttpEntity<>(headers);
    }
}
