package com.vintan.dto.response.community;

import com.vintan.embedded.CategoryRate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoryRateDto {
    private Integer cleanness;  // 청결도
    private Integer people;     // 유동인구
    private Integer reach;      // 접근성
    private Integer rentFee;    // 임대료

    public CategoryRateDto(CategoryRate categoryRate) {
        this.cleanness = categoryRate.getCleanness();
        this.people = categoryRate.getPeople();
        this.reach = categoryRate.getReach();
        this.rentFee = categoryRate.getRentFee();
    }
}
