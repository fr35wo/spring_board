package com.example.soulware_week1.board.exception;


import com.example.soulware_week1.global.error.exception.NotFoundGroupException;

public class CommentNotFoundException extends NotFoundGroupException {
    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(Long commentId) {
        this("해당하는 댓글이 없습니다. CommentID: " + commentId);
    }

    public CommentNotFoundException() {
        this("존재하지 않는 댓글입니다.");
    }
}
