package com.vintan.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaCommentRequest {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String comment; // Request Query로 받을 댓글 내용
}
