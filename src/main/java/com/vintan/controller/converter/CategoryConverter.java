package com.vintan.controller.converter;

import java.util.Map;

/**
 * Utility class for converting Korean category names to corresponding category codes.
 * This is typically used for mapping user-friendly names to standardized codes
 * for API requests or database storage.
 */
public class CategoryConverter {

    /**
     * A static map that holds the mapping of Korean category names to category codes.
     * Example: "대형마트" -> "MT1"
     */
    private static final Map<String, String> CATEGORY_MAP = Map.ofEntries(
            Map.entry("대형마트", "MT1"),   // Large mart
            Map.entry("편의점", "CS2"),    // Convenience store
            Map.entry("어린이집", "PS3"),  // Daycare center
            Map.entry("유치원", "PS3"),    // Kindergarten
            Map.entry("학교", "SC4"),      // School
            Map.entry("학원", "AC5"),      // Academy
            Map.entry("주차장", "PK6"),    // Parking lot
            Map.entry("주유소", "OL7"),    // Gas station
            Map.entry("충전소", "OL7"),    // Charging station
            Map.entry("지하철역", "SW8"),  // Subway station
            Map.entry("은행", "BK9"),      // Bank
            Map.entry("문화시설", "CT1"),  // Cultural facility
            Map.entry("중개업소", "AG2"),  // Real estate agency
            Map.entry("공공기관", "PO3"),  // Public institution
            Map.entry("관광명소", "AT4"),  // Tourist attraction
            Map.entry("숙박", "AD5"),      // Accommodation
            Map.entry("음식점", "FD6"),    // Restaurant
            Map.entry("카페", "CE7"),      // Cafe
            Map.entry("병원", "HP8"),      // Hospital
            Map.entry("약국", "PM9")       // Pharmacy
    );

    /**
     * Retrieves the category code for a given Korean category name.
     *
     * @param koreanName the Korean category name (e.g., "대형마트")
     * @return the corresponding category code (e.g., "MT1"), or null if not found
     */
    public static String getCategoryCode(String koreanName) {
        return CATEGORY_MAP.get(koreanName);
    }

}
