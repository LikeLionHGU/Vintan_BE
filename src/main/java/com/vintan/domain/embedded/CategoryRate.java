package com.vintan.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable entity representing category-based ratings for a location.
 * Includes scores for cleanness, floating population, accessibility, and rent fee.
 */
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRate {

    private Integer cleanness;  // Cleanliness rating
    private Integer people;     // Floating population rating
    private Integer reach;      // Accessibility rating
    private Integer rentFee;    // Rent fee rating
}
