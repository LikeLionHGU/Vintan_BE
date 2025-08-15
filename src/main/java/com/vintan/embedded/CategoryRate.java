package com.vintan.embedded;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRate {

    private Integer cleanness;  // 청결도
    private Integer people;     // 유동인구
    private Integer reach;      // 접근성
    private Integer rentFee;    // 임대료
}
