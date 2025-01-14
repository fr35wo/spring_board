package com.example.soulware_week1.global.jwt.api.dto.req;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenReqDto(
        @NotBlank(message = "Refresh Token은 비워둘 수 없습니다.")
        String refreshToken
) {
}
