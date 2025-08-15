package com.vintan.dto.response.ask;

// 응답 DTO: isSuccess (1: 성공, 0: 실패)
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleSuccessResponseDto {
    private int isSuccess; // 1 or 0

    public static SimpleSuccessResponseDto ok() {
        return new SimpleSuccessResponseDto(1);
    }

    public static SimpleSuccessResponseDto fail() {
        return new SimpleSuccessResponseDto(0);
    }
}