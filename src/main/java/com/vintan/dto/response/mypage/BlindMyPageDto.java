package com.vintan.dto.response.mypage;

import com.vintan.dto.response.community.CategoryRateDto;
import lombok.*;

/**
 * DTO for displaying blind community posts on the MyPage screen.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlindMyPageDto {

    private Long id;                    // Post ID

    private Double totalRate;           // Overall rating

    private String title;               // Post title

    private String address;             // Business/region address

    private String date;                // Post creation date (yyyy.MM.dd)

    private CategoryRateDto categoryRate; // Ratings by category

    private String positive;            // Positive aspects

    private String negative;            // Negative aspects

    private Long regionId;
}
