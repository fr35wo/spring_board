package com.example.soulware_week1.board.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BoardSaveReqDto(
        @NotBlank
        String title,

        @NotBlank
        String contents
) {
}
