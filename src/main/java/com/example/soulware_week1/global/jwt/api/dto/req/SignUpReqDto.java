package com.example.soulware_week1.global.jwt.api.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SignUpReqDto(
        @NotBlank String username,
        @NotBlank String password,
        String nickname
) {
}
