package com.vintan.dto.request.ask;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for creating a new community question post (Ask).
 * Only requires title and content from the user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAskRequestDto {

    @NotBlank(message = "Title is required")
    private String title;   // The title of the question/post

    @NotBlank(message = "Content is required")
    private String content; // The content/body of the question/post
}
