package com.example.soulware_week1.global.jwt.exception;


import com.example.soulware_week1.global.error.exception.AuthGroupException;

public class AuthorityNotFoundException extends AuthGroupException {
    public AuthorityNotFoundException(String message) {
        super(message);
    }

    public AuthorityNotFoundException() {
        this("권한 정보가 없는 토큰입니다.");
    }
}
