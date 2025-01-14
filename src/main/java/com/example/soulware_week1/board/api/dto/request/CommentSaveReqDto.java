package com.example.soulware_week1.board.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentSaveReqDto(
        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
        String content
) {
}
