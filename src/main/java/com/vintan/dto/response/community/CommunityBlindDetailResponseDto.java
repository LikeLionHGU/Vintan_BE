package com.vintan.dto.response.community;

import com.vintan.domain.BlindCommunityPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

/**
 * DTO representing the detailed view of a blind community post.
 */
@Getter
@AllArgsConstructor
public class CommunityBlindDetailResponseDto {

    private String title;            // Post title
    private String positive;         // Positive feedback content
    private String negative;         // Negative feedback content
    private String address;          // Address of the post
    private String date;             // Post creation date (yyyy.MM.dd)
    private CategoryRateDto categoryRate; // Ratings for cleanliness, people, accessibility, rent fee
    private String addressName;

    public CommunityBlindDetailResponseDto(BlindCommunityPost post) {
        this.title = post.getTitle();
        this.positive = post.getPositive();
        this.negative = post.getNegative();
        this.address = post.getAddress();
        this.date = post.getRegDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.categoryRate = new CategoryRateDto(post.getCategoryRate());
    }
}
