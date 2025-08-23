package com.vintan.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for creating a comment on a post.
 * Only requires the comment text from the user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequestDto {

    @NotBlank(message = "Comment is required")
    private String comment; // The content of the comment
}
