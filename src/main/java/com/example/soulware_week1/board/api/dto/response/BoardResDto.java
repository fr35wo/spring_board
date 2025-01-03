package com.example.soulware_week1.board.api.dto.response;

import com.example.soulware_week1.board.domain.Board;
import lombok.Builder;

@Builder
public record BoardResDto(
        Long boardId,
        String title,
        String contents
) {
    public static BoardResDto of(Board board) {
        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .contents(board.getContents())
                .build();
    }
}
