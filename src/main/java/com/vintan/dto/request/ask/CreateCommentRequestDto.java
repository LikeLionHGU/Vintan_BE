package com.vintan.dto.request.ask;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequestDto {
    @NotBlank(message = "comment is required")
    private String comment; // 댓글 내용
}
