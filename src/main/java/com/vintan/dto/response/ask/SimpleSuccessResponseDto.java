package com.vintan.dto.response.ask;

import lombok.*;

/**
 * Simple response DTO indicating success or failure of an operation.
 * isSuccess = 1 → success
 * isSuccess = 0 → failure
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleSuccessResponseDto {

    /** Success flag: 1 = success, 0 = failure */
    private int isSuccess;

    /** Factory method for success response */
    public static SimpleSuccessResponseDto ok() {
        return new SimpleSuccessResponseDto(1);
    }

    /** Factory method for failure response */
    public static SimpleSuccessResponseDto fail() {
        return new SimpleSuccessResponseDto(0);
    }
}
