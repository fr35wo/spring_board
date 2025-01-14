package com.example.soulware_week1.global.jwt.api.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SignInReqDto(
        @NotBlank(message = "아이디는 비워둘 수 없습니다.")
        String username,
        @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
        String password
) {
}
