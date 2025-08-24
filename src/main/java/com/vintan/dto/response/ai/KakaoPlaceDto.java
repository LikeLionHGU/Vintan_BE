package com.vintan.dto.response.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO representing a place returned from the Kakao API.
 */
@Getter
@AllArgsConstructor
public class KakaoPlaceDto {

    private String name;      // Name of the place
    private String address;   // Address of the place
    private String category;  // Category of the place
}
