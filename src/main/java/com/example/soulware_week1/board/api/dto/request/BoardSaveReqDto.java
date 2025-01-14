package com.example.soulware_week1.board.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BoardSaveReqDto(
        @NotBlank(message = "게시글 제목은 비워둘 수 없습니다.")
        String title,

        @NotBlank(message = "게시글 내용은 비워둘 수 없습니다.")
        String contents
) {
}
