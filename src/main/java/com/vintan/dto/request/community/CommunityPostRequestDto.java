package com.vintan.dto.request.community;

import com.vintan.domain.embedded.CategoryRate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * DTO for creating or updating a community post.
 * Includes the title, positive/negative feedback, optional address, and category ratings.
 */
@Getter
public class CommunityPostRequestDto {

    @NotBlank(message = "Title is required")
    private String title; // Post title

    @NotBlank(message = "Positive feedback is required")
    private String positive; // Positive points about the subject

    @NotBlank(message = "Negative feedback is required")
    private String negative; // Negative points about the subject

    private String address; // Optional address for the post

    @NotNull(message = "Category rate is required")
    private CategoryRate categoryRate; // Ratings for cleanliness, foot traffic, accessibility, and rent fee
}
