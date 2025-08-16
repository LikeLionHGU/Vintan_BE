package com.vintan.dto.response.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoPlaceDto {
    private String name;
    private String address;
    private String category;
}
