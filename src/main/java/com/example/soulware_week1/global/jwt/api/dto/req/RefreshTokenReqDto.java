package com.example.soulware_week1.global.jwt.api.dto.req;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenReqDto(
        @NotBlank
        String refreshToken
) {
}
