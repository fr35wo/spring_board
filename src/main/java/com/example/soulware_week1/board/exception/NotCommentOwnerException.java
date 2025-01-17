package com.example.soulware_week1.board.exception;

import com.example.soulware_week1.global.error.exception.AccessDeniedGroupException;

public class NotCommentOwnerException extends AccessDeniedGroupException {
    public NotCommentOwnerException(String message) {
        super(message);
    }

    public NotCommentOwnerException() {
        this("본인이 아니면 댓글을 조작할 수 없습니다.");
    }
}
