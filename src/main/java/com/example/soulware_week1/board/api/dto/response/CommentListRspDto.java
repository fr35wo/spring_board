package com.example.soulware_week1.board.api.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record CommentListRspDto(
        List<CommentResDto> content,
        int page,
        int size,
        int totalPages,
        long totalElements
) {
    public static CommentListRspDto of(Page<CommentResDto> page) {
        return new CommentListRspDto(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}


