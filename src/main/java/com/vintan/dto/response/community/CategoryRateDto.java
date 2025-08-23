package com.vintan.dto.response.community;

import com.vintan.domain.embedded.CategoryRate;
import lombok.*;

/**
 * DTO representing category ratings for a post or report.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRateDto {

    private Integer cleanness;  // Cleanliness rating
    private Integer people;     // Foot traffic / people rating
    private Integer reach;      // Accessibility rating
    private Integer rentFee;    // Rent/lease cost rating

    /**
     * Constructs a CategoryRateDto from a CategoryRate embedded entity.
     *
     * @param categoryRate the CategoryRate entity
     */
    public CategoryRateDto(CategoryRate categoryRate) {
        this.cleanness = categoryRate.getCleanness();
        this.people = categoryRate.getPeople();
        this.reach = categoryRate.getReach();
        this.rentFee = categoryRate.getRentFee();
    }
}
