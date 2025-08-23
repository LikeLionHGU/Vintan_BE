package com.vintan.dto.response.ai.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO for Kakao Address API response.
 * Wraps a list of address documents containing latitude and longitude.
 */
public record KakaoAddressResponse(
        @JsonProperty("documents") List<Document> documents
) {

    /**
     * Represents a single address document returned by the Kakao API.
     */
    public record Document(
            @JsonProperty("x") String longitude,  // Longitude of the address
            @JsonProperty("y") String latitude    // Latitude of the address
    ) {}
}
