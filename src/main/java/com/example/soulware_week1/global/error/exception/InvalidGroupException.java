package com.example.soulware_week1.global.error.exception;

public abstract class InvalidGroupException extends IllegalArgumentException {
    public InvalidGroupException(String message) {
        super(message);
    }
}
