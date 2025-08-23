package com.vintan.dto.response.ask;

import lombok.*;

import java.util.List;

/**
 * DTO representing a list of Ask posts.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AskResponseDto {

    /** List of AskDto objects representing individual posts */
    private List<AskDto> askList;
}
