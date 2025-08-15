package com.vintan.dto.response.ask;

// 상세 Ask DTO
import com.vintan.domain.QnaComment;
import com.vintan.domain.QnaPost;
import lombok.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AskDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String userId;
    private String date;                 // yyyy.MM.dd
    private List<CommentDto> commentList;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static AskDetailResponseDto from(QnaPost p, List<QnaComment> comments) {
        return AskDetailResponseDto.builder()
                .id(p.getPostId())
                .title(p.getTitle())
                .content(p.getContent())
                .userId(p.getUser().getId())
                .date(p.getCreatedAt() == null ? null : p.getCreatedAt().format(FMT))
                .commentList(comments.stream().map(CommentDto::from).toList())
                .build();
    }
}

