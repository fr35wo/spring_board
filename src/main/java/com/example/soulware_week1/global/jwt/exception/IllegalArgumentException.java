package com.example.soulware_week1.global.jwt.exception;

import com.example.soulware_week1.global.error.exception.InvalidGroupException;

public class IllegalArgumentException extends InvalidGroupException {
    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException() {
        this("이미 사용 중인 사용자 이름입니다.");
    }
}

