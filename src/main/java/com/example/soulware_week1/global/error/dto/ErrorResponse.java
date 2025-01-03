package com.example.soulware_week1.global.error.dto;

public record ErrorResponse(
        int statusCode,
        String message
) {
}