package com.example.soulware_week1.board.api.dto.response;

import com.example.soulware_week1.board.domain.Comment;
import java.time.LocalDateTime;
import lombok.Builder;


@Builder
public record CommentResDto(
        Long commentId,
        Long boardId,
        String content,
        LocalDateTime createAt,
        String writer
) {
    public static CommentResDto from(Comment comment) {
        return new CommentResDto(
                comment.getCommentId(),
                comment.getBoard().getBoardId(),
                comment.getContent(),
                comment.getCreateAt(),
                comment.getWriter().getNickname()
        );
    }

}

