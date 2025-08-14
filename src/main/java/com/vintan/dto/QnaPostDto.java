package com.vintan.dto;

import com.vintan.domain.QnaPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaPostDto {

    private Integer postId;

    @NotNull(message = "userid는 필수입니다.")
    private String userId;

    @NotBlank(message = "title은 비어 있을 수 없습니다.")
    private String title;

    @NotBlank(message = "content는 비어 있을 수 없습니다.")
    private String content;


    // Entity -> DTO
    public static QnaPostDto fromEntity(QnaPost entity) {
        if (entity == null) return null;
        return QnaPostDto.builder()
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }

    // DTO -> Entity (신규 생성 시 postId는 null이어도 됨)
    public QnaPost toEntity() {
        return QnaPost.builder()
                .postId(this.postId)   // null이면 JPA가 IDENTITY로 생성
                .userId(this.userId)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
