package com.example.soulware_week1.board.exception;

import com.example.soulware_week1.global.error.exception.InvalidGroupException;

public class InvalidPageException extends InvalidGroupException {
    public InvalidPageException(String message) {
        super(message);
    }

    public InvalidPageException(int totalPages) {
        this("요청한 페이지가 유효하지 않습니다. 총 페이지 수: " + totalPages);
    }

    public InvalidPageException(int page, int size) {
        this("페이지와 크기는 1 이상이어야 합니다. page: " + page + ", size: " + size);
    }
}
