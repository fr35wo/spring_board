package com.example.soulware_week1.board.api.dto.response;

import com.example.soulware_week1.board.domain.Board;
import lombok.Builder;

@Builder
public record BoardDto(
        Long boardId,
        String title,
        String contents
) {
    public static BoardDto fromEntity(Board board) {

        return BoardDto.builder()
                .boardId(board.getBoardId())
                .contents(board.getContents())
                .title(board.getTitle())
                .build();
    }
}

