package com.vintan.dto.response.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record KakaoCategoryResponse(
        @JsonProperty("documents") List<PlaceDocument> documents
) {
    public record PlaceDocument(
            @JsonProperty("place_name") String placeName,
            @JsonProperty("road_address_name") String roadAddressName,
            @JsonProperty("category_name") String categoryName
    ) {}
}