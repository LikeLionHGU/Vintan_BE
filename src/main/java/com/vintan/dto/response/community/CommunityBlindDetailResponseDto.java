package com.vintan.dto.response.community;

import com.vintan.domain.BlindCommunityPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class CommunityBlindDetailResponseDto {
    private String title;
    private String positive;
    private String negative;
    private String address;
    private String date;
    private CategoryRateDto categoryRate;

    public CommunityBlindDetailResponseDto(BlindCommunityPost post) {
        this.title = post.getTitle();
        this.positive = post.getPositive();
        this.negative = post.getNegative();
        this.address = post.getAddress();
        this.date = post.getRegDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.categoryRate = new CategoryRateDto(post.getCategoryRate());
    }
}
