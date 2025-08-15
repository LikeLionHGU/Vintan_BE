package com.vintan.dto.response.ask;

// 댓글 DTO
import com.vintan.domain.QnaComment;
import lombok.*;
import java.time.format.DateTimeFormatter;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CommentDto {
    private Long id;        // 댓글 고유 ID (Long 권장)
    private String userId;  // 댓글 작성자 ID
    private String content; // 댓글 내용
    private String date;    // yyyy.MM.dd

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static CommentDto from(QnaComment c) {
        return CommentDto.builder()
                .id(c.getId())
                .userId(c.getUser().getId())
                .content(c.getText())
                .date(c.getTextTime() == null ? null : c.getTextTime().format(FMT))
                .build();
    }
}