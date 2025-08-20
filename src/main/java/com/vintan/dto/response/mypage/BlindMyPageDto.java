
package com.vintan.dto.response.mypage;

import com.vintan.dto.response.community.CategoryRateDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlindMyPageDto {


    private Long id;               // Post ID


    private Double totalRate;      // 전체 평점


    private String title;          // 글 제목


    private String address;        // 상권 주소


    private String date;           // yyyy.mm.dd


    private CategoryRateDto categoryRate; // 카테고리별 평점


    private String positive;       // 장점


    private String negative;       // 단점


}