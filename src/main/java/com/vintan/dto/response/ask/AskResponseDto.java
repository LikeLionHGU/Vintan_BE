package com.vintan.dto.response.ask;

import lombok.*;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AskResponseDto {
    private List<AskDto> askList;
}
