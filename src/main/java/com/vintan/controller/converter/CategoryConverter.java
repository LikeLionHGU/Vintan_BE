package com.vintan.controller.converter;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for converting Korean category names to corresponding category codes
 * and vice versa.
 */
public class CategoryConverter {

    private static final Map<String, String> CATEGORY_MAP = Map.ofEntries(
            Map.entry("대형마트", "MT1"),
            Map.entry("편의점", "CS2"),
            Map.entry("어린이집", "PS3"),
            Map.entry("유치원", "PS3"),
            Map.entry("학교", "SC4"),
            Map.entry("학원", "AC5"),
            Map.entry("주차장", "PK6"),
            Map.entry("주유소", "OL7"),
            Map.entry("충전소", "OL7"),
            Map.entry("지하철역", "SW8"),
            Map.entry("은행", "BK9"),
            Map.entry("문화시설", "CT1"),
            Map.entry("중개업소", "AG2"),
            Map.entry("공공기관", "PO3"),
            Map.entry("관광명소", "AT4"),
            Map.entry("숙박", "AD5"),
            Map.entry("음식점", "FD6"),
            Map.entry("카페", "CE7"),
            Map.entry("병원", "HP8"),
            Map.entry("약국", "PM9")
    );

    // ✅ 역맵 생성 (코드 → 한국어 이름)
    private static final Map<String, String> CODE_MAP = CATEGORY_MAP.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (existing, replacement) -> existing));

    /**
     * Retrieves the category code for a given Korean category name.
     *
     * @param koreanName the Korean category name (e.g., "대형마트")
     * @return the corresponding category code (e.g., "MT1"), or null if not found
     */
    public static String getCategoryCode(String koreanName) {
        return CATEGORY_MAP.get(koreanName);
    }

    /**
     * Retrieves the Korean category name for a given category code.
     *
     * @param code the category code (e.g., "MT1")
     * @return the corresponding Korean category name (e.g., "대형마트"), or null if not found
     */
    public static String getKoreanName(String code) {
        return CODE_MAP.get(code);
    }

}
