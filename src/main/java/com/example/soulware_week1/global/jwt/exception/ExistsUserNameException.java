package com.example.soulware_week1.global.jwt.exception;

import com.example.soulware_week1.global.error.exception.InvalidGroupException;

public class ExistsUserNameException extends InvalidGroupException {
    public ExistsUserNameException(String message) {
        super(message);
    }

    public ExistsUserNameException() {
        this("이미 존재하는 이메일입니다.");
    }
}
