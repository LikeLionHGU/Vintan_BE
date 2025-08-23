package com.vintan.dto.response.ai.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO for Kakao Category Search API response.
 * Contains a list of places under the specified category.
 */
public record KakaoCategoryResponse(
        @JsonProperty("documents") List<PlaceDocument> documents
) {

    /**
     * Represents a single place document returned by the Kakao API.
     */
    public record PlaceDocument(
            @JsonProperty("place_name") String placeName,           // Name of the place
            @JsonProperty("road_address_name") String roadAddressName, // Road address of the place
            @JsonProperty("category_name") String categoryName       // Category name of the place
    ) {}
}
