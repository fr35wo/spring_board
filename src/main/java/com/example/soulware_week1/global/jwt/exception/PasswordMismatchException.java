package com.example.soulware_week1.global.jwt.exception;

import com.example.soulware_week1.global.error.exception.InvalidGroupException;

public class PasswordMismatchException extends InvalidGroupException {
    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException() {
        this("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
}

