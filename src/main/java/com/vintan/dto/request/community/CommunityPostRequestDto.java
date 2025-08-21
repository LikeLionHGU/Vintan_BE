package com.vintan.dto.request.community;

import com.vintan.embedded.CategoryRate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommunityPostRequestDto {
    @NotBlank(message = "title Compulsory")
    private String title;

    @NotBlank(message = "positive Compulsory")
    private String positive;

    @NotBlank(message = "negative Compulsory")
    private String negative;

    private String address;

    @NotNull(message = "Category Rate Compulsory")
    private CategoryRate categoryRate;
}
