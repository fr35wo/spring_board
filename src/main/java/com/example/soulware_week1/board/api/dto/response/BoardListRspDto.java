package com.example.soulware_week1.board.api.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record BoardListRspDto(
        List<BoardResDto> content,
        int page,
        int size,
        int totalPages,
        long totalElements
) {
    public static BoardListRspDto of(Page<BoardResDto> page) {
        return new BoardListRspDto(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
