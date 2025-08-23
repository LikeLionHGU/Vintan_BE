package com.vintan.dto.response.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record KakaoAddressResponse(
        @JsonProperty("documents") List<Document> documents
) {
    public record Document(
            @JsonProperty("x") String longitude,
            @JsonProperty("y") String latitude
    ) {}
}