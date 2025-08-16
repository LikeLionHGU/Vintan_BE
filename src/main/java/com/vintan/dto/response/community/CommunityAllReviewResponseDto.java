package com.vintan.dto.response.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommunityAllReviewResponseDto {
    private double totalRate;
    private double clean;
    private double people;
    private double accessibility;
    private double rentFee;
    private List<BlindSummaryDto> blind;

}
